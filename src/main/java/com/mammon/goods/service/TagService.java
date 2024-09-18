package com.mammon.goods.service;

import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.common.ResultCode;
import com.mammon.enums.CommonStatus;
import com.mammon.exception.CustomException;
import com.mammon.goods.dao.TagDao;
import com.mammon.goods.domain.dto.TagDto;
import com.mammon.goods.domain.entity.TagEntity;
import com.mammon.goods.domain.query.TagQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TagService {

    @Resource
    private TagDao tagDao;

    public void create(long merchantNo, TagDto dto) {
        TagEntity tagEntity = findByMerchantNoAndName(merchantNo, dto.getName().trim());
        if (tagEntity != null) {
            throw new CustomException(ResultCode.BAD_REQUEST, "标签名重复");
        }
        TagEntity entity = new TagEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setName(dto.getName().trim());
        entity.setRemark(dto.getRemark());
        entity.setSort(dto.getSort());
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        tagDao.save(entity);
    }

    public void edit(long merchantNo, String id, TagDto dto) {
        TagEntity entity = tagDao.findById(merchantNo, id);
        if (entity == null) {
            return;
        }
        TagEntity existsTag = tagDao.findByMerchantNoAndName(merchantNo, dto.getName());
        if (existsTag != null && !existsTag.getId().equals(id)) {
            throw new CustomException("标签名重复");
        }
        entity.setName(dto.getName().trim());
        entity.setRemark(dto.getRemark());
        entity.setSort(dto.getSort());
        entity.setUpdateTime(LocalDateTime.now());
        tagDao.update(entity);
    }

    public void delete(long merchantNo, String id) {
        TagEntity entity = info(merchantNo, id);
        if (entity == null) {
            throw new CustomException(ResultCode.BAD_REQUEST, "标签信息为空");
        }
        tagDao.delete(merchantNo, id);
    }

    public TagEntity info(long merchantNo, String id) {
        return tagDao.findById(merchantNo, id);
    }

    public TagEntity findByMerchantNoAndName(long merchantNo, String name) {
        return tagDao.findByMerchantNoAndName(merchantNo, name);
    }

    public List<TagEntity> findAllByIds(long merchantNo, List<String> ids) {
        return tagDao.findAllByIds(merchantNo, ids);
    }

    public PageVo<TagEntity> page(long merchantNo,
                                  String name,
                                  TagQuery query) {
        int total = tagDao.count(merchantNo, name);
        if (total <= 0) {
            return null;
        }
        List<TagEntity> list = tagDao.findPage(merchantNo, name, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, list);
    }

    public List<TagEntity> findAll(long merchantNo) {
        return tagDao.findAll(merchantNo);
    }
}
