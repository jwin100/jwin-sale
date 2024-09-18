package com.mammon.goods.service;

import com.mammon.common.*;
import com.mammon.enums.CommonStatus;
import com.mammon.exception.CustomException;
import com.mammon.goods.dao.SpecDao;
import com.mammon.goods.domain.dto.SpecDto;
import com.mammon.goods.domain.dto.SpecValueDto;
import com.mammon.goods.domain.entity.SkuSpecEntity;
import com.mammon.goods.domain.entity.SpecEntity;
import com.mammon.goods.domain.query.SpecKeyQuery;
import com.mammon.goods.domain.vo.SpecKeyVo;
import com.mammon.goods.domain.vo.SpecValueVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SpecService {
    @Resource
    private SpecDao specDao;

    @Resource
    private SkuSpecService skuSpecService;

    public SpecEntity create(long merchantNo, SpecDto dto) {
        SpecEntity specEntity = findChildByName(merchantNo, dto.getName(), dto.getPid());
        if (specEntity != null) {
            if (StringUtils.isBlank(dto.getPid())) {
                throw new CustomException("规格项重复");
            }
            throw new CustomException("规格值重复");
        }
        SpecEntity entity = new SpecEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setName(dto.getName().trim());
        entity.setPid(dto.getPid());
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        specDao.save(entity);
        return entity;
    }

    public void edit(long merchantNo, String id, SpecDto dto) {
        SpecEntity entity = specDao.findById(merchantNo, id);
        if (entity == null) {
            return;
        }
        SpecEntity specEntity = findChildByName(merchantNo, dto.getName(), dto.getPid());
        if (specEntity != null && !specEntity.getId().equals(id)) {
            if (StringUtils.isBlank(dto.getPid())) {
                throw new CustomException("规格项重复");
            }
            throw new CustomException("规格值重复");
        }
        entity.setId(id);
        entity.setMerchantNo(merchantNo);
        entity.setName(dto.getName());
        entity.setPid(dto.getPid());
        entity.setUpdateTime(LocalDateTime.now());
        specDao.update(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchEdit(long merchantNo, List<SpecValueDto> dtos) {
        dtos.forEach(x -> {
            SpecDto specDto = new SpecDto();
            specDto.setName(x.getName());
            specDto.setPid(x.getPid());
            if (StringUtils.isBlank(x.getId())) {
                create(merchantNo, specDto);
            } else {
                edit(merchantNo, x.getId(), specDto);
            }
        });
    }

    public void delete(long merchantNo, String id) {
        List<SkuSpecEntity> list = skuSpecService.findAllBySpecId(id);
        if (!CollectionUtils.isEmpty(list)) {
            throw new CustomException("无法删除使用中的规格");
        }
        specDao.delete(merchantNo, id);
    }

    public SpecEntity findById(long merchantNo, String id) {
        return specDao.findById(merchantNo, id);
    }

    public SpecEntity findChildByName(long merchantNo, String name, String pid) {
        return specDao.findByMerchantNoAndName(merchantNo, name, pid);
    }

    public List<SpecEntity> findAllByIds(long merchantNo, List<String> ids) {
        return specDao.findAllByIds(merchantNo, ids);
    }

    public List<SpecEntity> findAll(long merchantNo, SpecKeyQuery query) {
        return specDao.findAll(merchantNo, query);
    }

    public PageVo<SpecKeyVo> page(long merchantNo,
                                  SpecKeyQuery query) {
        int total = specDao.count(merchantNo, query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<SpecEntity> list = specDao.findPage(merchantNo, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<SpecKeyVo> keyVos = list.stream().map(x -> {
            SpecKeyVo keyVo = new SpecKeyVo();
            BeanUtils.copyProperties(x, keyVo);

            List<SpecEntity> values = specDao.findAllByPid(merchantNo, x.getId());
            List<SpecValueVo> valueVos = values.stream().map(y -> {
                SpecValueVo valueVo = new SpecValueVo();
                BeanUtils.copyProperties(y, valueVo);
                return valueVo;
            }).collect(Collectors.toList());
            keyVo.setValues(valueVos);
            return keyVo;
        }).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, keyVos);
    }
}
