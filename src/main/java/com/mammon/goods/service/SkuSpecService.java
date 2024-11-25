package com.mammon.goods.service;

import com.mammon.common.Generate;
import com.mammon.enums.CommonStatus;
import com.mammon.goods.dao.SkuSpecDao;
import com.mammon.goods.domain.dto.SkuSpecDto;
import com.mammon.goods.domain.entity.SkuSpecEntity;
import com.mammon.goods.domain.vo.SkuSpecVo;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SkuSpecService {

    @Resource
    private SkuSpecDao skuSpecDao;

    @Transactional(rollbackFor = Exception.class)
    public void batchSave(String spuId, String skuId, List<SkuSpecDto> dtos) {
        List<SkuSpecVo> list = findAllBySkuId(skuId);
        dtos.forEach(x -> {
            SkuSpecVo skuSpecVo = list.stream().filter(y -> y.getSpecValueId().equals(x.getSpecValueId()))
                    .findFirst().orElse(null);
            if (skuSpecVo == null) {
                log.info("保存商品规格信息：spu:{},sku:{}", spuId, skuId);
                save(spuId, skuId, x);
            }
        });
    }

    public void save(String spuId, String skuId, SkuSpecDto dto) {
        SkuSpecEntity entity = new SkuSpecEntity();
        entity.setId(Generate.generateUUID());
        entity.setSpuId(spuId);
        entity.setSkuId(skuId);
        entity.setSpecId(dto.getSpecId());
        entity.setSpecName(dto.getSpecName());
        entity.setSpecValueId(dto.getSpecValueId());
        entity.setSpecValueName(dto.getSpecValueName());
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

    public List<SkuSpecVo> findAllBySkuId(String skuId) {
        List<SkuSpecEntity> list = skuSpecDao.findAllBySkuId(skuId);
        return list.stream().map(x -> {
            SkuSpecVo vo = new SkuSpecVo();
            BeanUtils.copyProperties(x, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    public List<SkuSpecVo> findAllBySkuIds(List<String> skuIds) {
        if (CollectionUtils.isEmpty(skuIds)) {
            return Collections.emptyList();
        }
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
