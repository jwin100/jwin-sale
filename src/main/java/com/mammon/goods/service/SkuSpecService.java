package com.mammon.goods.service;

import com.mammon.common.Generate;
import com.mammon.enums.CommonStatus;
import com.mammon.goods.dao.SkuSpecDao;
import com.mammon.goods.domain.dto.SkuSpecDto;
import com.mammon.goods.domain.entity.SkuSpecEntity;
import com.mammon.goods.domain.vo.SkuSpecVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @date 2023-03-22 16:56:33
 */
@Service
public class SkuSpecService {

    @Resource
    private SkuSpecDao skuSpecDao;

    @Transactional(rollbackFor = Exception.class)
    public void batchSave(String spuId, String skuId, List<SkuSpecDto> dtos) {
        dtos.forEach(x -> {
            save(spuId, skuId, x);
        });
    }

    public void save(String spuId, String skuId, SkuSpecDto dto) {
        SkuSpecEntity entity = new SkuSpecEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setSpuId(spuId);
        entity.setSkuId(skuId);
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        skuSpecDao.save(entity);
    }

    public void deleteBySpuId(String spuId) {
        skuSpecDao.deleteBySpuId(spuId);
    }

    public void deleteBySkuId(String skuId) {
        skuSpecDao.deleteBySkuId(skuId);
    }

    public List<SkuSpecVo> findAllBySpuId(String spuId, String skuId) {
        List<SkuSpecEntity> list = skuSpecDao.findAllBySpuId(spuId, skuId);
        return list.stream().map(x -> {
            SkuSpecVo vo = new SkuSpecVo();
            BeanUtils.copyProperties(x, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    public List<SkuSpecVo> findAllBySkuIds(List<String> skuIds) {
        List<SkuSpecEntity> list = skuSpecDao.findAllBySkuIds(skuIds);
        return list.stream().map(x -> {
            SkuSpecVo vo = new SkuSpecVo();
            BeanUtils.copyProperties(x, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    public List<SkuSpecVo> findAllBySpuIds(List<String> spuIds) {
        if (CollectionUtils.isEmpty(spuIds)) {
            return Collections.emptyList();
        }
        List<SkuSpecEntity> list = skuSpecDao.findAllBySpuIds(spuIds);
        return list.stream().map(x -> {
            SkuSpecVo vo = new SkuSpecVo();
            BeanUtils.copyProperties(x, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    public List<SkuSpecEntity> findAllBySpecId(String specId) {
        return skuSpecDao.findAllBySpecId(specId);
    }
}
