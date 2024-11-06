package com.mammon.goods.service;

import cn.hutool.core.collection.CollUtil;
import com.mammon.common.*;
import com.mammon.enums.CommonDeleted;
import com.mammon.enums.CommonStatus;
import com.mammon.exception.CustomException;
import com.mammon.goods.dao.CategoryDao;
import com.mammon.goods.domain.dto.CategoryDto;
import com.mammon.goods.domain.dto.CategoryImportDto;
import com.mammon.goods.domain.entity.CategoryEntity;
import com.mammon.goods.domain.enums.CategoryLevel;
import com.mammon.goods.domain.query.CategoryListQuery;
import com.mammon.goods.domain.query.CategoryQuery;
import com.mammon.goods.domain.query.SpuQuery;
import com.mammon.goods.domain.vo.CategoryTreeVo;
import com.mammon.goods.domain.vo.CategoryVo;
import com.mammon.goods.domain.vo.SpuListVo;
import com.mammon.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryService {

    @Resource
    private CategoryDao categoryDao;

    @Resource
    private SpuService spuService;

    public CategoryEntity create(long merchantNo, CategoryDto dto) {
        dto.setPid(StringUtils.isNotBlank(dto.getPid()) ? dto.getPid() : null);
        int level = CategoryLevel.ONE.getCode();
        if (StringUtils.isNotBlank(dto.getPid())) {
            CategoryEntity parentClassify = findById(merchantNo, dto.getPid());
            if (parentClassify == null) {
                throw new CustomException(ResultCode.BAD_REQUEST, "上级分类错误");
            }
            if (parentClassify.getLevel() > CategoryLevel.ONE.getCode()) {
                throw new CustomException(ResultCode.BAD_REQUEST, "最大只能有二级分类");
            }
            level = CategoryLevel.TWO.getCode();
        }

        if (CategoryLevel.ONE.getCode() == level) {
            CategoryEntity categoryEntity = findByName(merchantNo, dto.getName(), null);
            if (categoryEntity != null) {
                throw new CustomException("分类名重复");
            }
        }
        if (CategoryLevel.TWO.getCode() == level) {
            CategoryEntity categoryEntity = findByName(merchantNo, dto.getName(), dto.getPid());
            if (categoryEntity != null) {
                throw new CustomException("分类名重复");
            }
        }
        CategoryEntity entity = new CategoryEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setLevel(level);
        categoryDao.save(entity);
        return entity;
    }

    public void edit(long merchantNo, String id, CategoryDto dto) {
        CategoryEntity categoryEntity = findByName(merchantNo, dto.getName().trim(), dto.getPid());
        if (categoryEntity != null && !categoryEntity.getId().equals(id)) {
            throw new CustomException(ResultCode.BAD_REQUEST, String.format("分类名称重复:%s", dto.getName()));
        }
        CategoryEntity entity = categoryDao.findById(merchantNo, id);
        if (entity == null) {
            return;
        }
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setUpdateTime(LocalDateTime.now());
        categoryDao.update(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void categoryImport(long merchantNo, MultipartFile file) {
        List<CategoryImportDto> list = ExcelUtils.read(file, CategoryImportDto.class);
        if (CollectionUtils.isEmpty(list)) {
            throw new CustomException("检测导入分类内容为空");
        }
        list.forEach(x -> {
            CategoryEntity oneCategoryEntity = findByName(merchantNo, x.getOneCategory(), null);
            if (oneCategoryEntity == null) {
                CategoryDto dto = new CategoryDto();
                dto.setName(x.getOneCategory());
                oneCategoryEntity = create(merchantNo, dto);
            }
            CategoryEntity twoCategoryEntity = findByName(merchantNo, x.getOneCategory(), oneCategoryEntity.getId());
            if (twoCategoryEntity == null) {
                CategoryDto dto = new CategoryDto();
                dto.setPid(oneCategoryEntity.getId());
                dto.setName(x.getTwoCategory());
                create(merchantNo, dto);
            }
        });
    }

    public void deleteById(long merchantNo, String id) {
        List<CategoryEntity> list = categoryDao.findSubAllByPid(merchantNo, id);
        if (!CollectionUtils.isEmpty(list)) {
            throw new CustomException("当前分类下有子分类，不能删除");
        }
        SpuQuery spuQuery = new SpuQuery();
        spuQuery.setCategoryId(id);
        List<SpuListVo> spus = spuService.list(merchantNo, spuQuery);
        if (!CollectionUtils.isEmpty(spus)) {
            throw new CustomException("无法删除使用中的分类");
        }
        categoryDao.delete(merchantNo, id);
    }

    public CategoryEntity findById(long merchantNo, String id) {
        return categoryDao.findById(merchantNo, id);
    }

    public PageVo<CategoryVo> page(long merchantNo,
                                   CategoryQuery query) {
        query.setLevel(CategoryLevel.ONE.getCode());
        int total = categoryDao.countPage(merchantNo, query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<CategoryEntity> list = categoryDao.findPage(merchantNo, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<CategoryVo> vos = list.stream().map(category -> {
            CategoryVo vo = new CategoryVo();
            BeanUtils.copyProperties(category, vo);

            CategoryListQuery subQuery = new CategoryListQuery();
            subQuery.setPid(category.getId());
            subQuery.setDeleted(CommonDeleted.NOT_DELETED.getCode());
            subQuery.setLevel(CategoryLevel.TWO.getCode());
            List<CategoryEntity> subList = findAll(merchantNo, subQuery);
            vo.setChildren(
                    subList.stream().map(subCategory -> {
                        CategoryVo subVo = new CategoryVo();
                        BeanUtils.copyProperties(subCategory, subVo);
                        return subVo;
                    }).collect(Collectors.toList())
            );
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }

    public List<CategoryTreeVo> tree(long merchantNo) {
        CategoryListQuery query = new CategoryListQuery();
        query.setLevel(CategoryLevel.ONE.getCode());
        query.setDeleted(CommonDeleted.NOT_DELETED.getCode());
        List<CategoryEntity> list = findAll(merchantNo, query);
        return list.stream().map(x -> {
            CategoryListQuery subQuery = new CategoryListQuery();
            subQuery.setPid(x.getId());
            subQuery.setLevel(CategoryLevel.TWO.getCode());
            subQuery.setDeleted(CommonDeleted.NOT_DELETED.getCode());
            List<CategoryEntity> subList = findAll(merchantNo, subQuery);

            List<CategoryTreeVo> childrenes = subList.stream().map(children -> {
                CategoryTreeVo childrenVo = new CategoryTreeVo();
                childrenVo.setValue(children.getId());
                childrenVo.setLabel(children.getName());
                childrenVo.setPid(children.getPid());
                childrenVo.setSort(children.getSort());
                return childrenVo;
            }).collect(Collectors.toList());

            CategoryTreeVo vo = new CategoryTreeVo();
            vo.setValue(x.getId());
            vo.setLabel(x.getName());
            vo.setPid(x.getPid());
            vo.setSort(x.getSort());

            if (CollUtil.isNotEmpty(childrenes)) {
                vo.setChildren(childrenes);
            }
            return vo;
        }).collect(Collectors.toList());
    }

    public CategoryEntity findByName(long merchantNo, String name, String pid) {
        int level = CategoryLevel.ONE.getCode();
        if (StringUtils.isNotBlank(pid)) {
            level = CategoryLevel.TWO.getCode();
        }
        return categoryDao.findByName(merchantNo, level, name, pid);
    }

    public List<CategoryEntity> findAll(long merchantNo, CategoryListQuery query) {
        return categoryDao.findAll(merchantNo, query);
    }
}
