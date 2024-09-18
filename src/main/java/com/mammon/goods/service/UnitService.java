package com.mammon.goods.service;

import com.mammon.common.*;
import com.mammon.enums.CommonStatus;
import com.mammon.exception.CustomException;
import com.mammon.goods.dao.UnitDao;
import com.mammon.goods.domain.dto.UnitDto;
import com.mammon.goods.domain.entity.UnitEntity;
import com.mammon.goods.domain.query.SpuQuery;
import com.mammon.goods.domain.query.UnitQuery;
import com.mammon.goods.domain.vo.SpuListVo;
import com.mammon.goods.domain.vo.UnitVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UnitService {

    @Resource
    private UnitDao unitDao;

    @Resource
    private SpuService spuService;

    public UnitEntity create(long merchantNo, UnitDto dto) {
        UnitVo unitVo = findByMerchantNoAndName(merchantNo, dto.getName().trim());
        if (unitVo != null) {
            throw new CustomException(ResultCode.BAD_REQUEST, "单位名重复");
        }
        UnitEntity entity = new UnitEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setName(dto.getName().trim());
        entity.setType(dto.getType());
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        unitDao.save(entity);
        return entity;
    }

    public void edit(long merchantNo, String id, UnitDto dto) {
        UnitEntity entity = unitDao.findById(merchantNo, id);
        if (entity == null) {
            return;
        }
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setUpdateTime(LocalDateTime.now());
        unitDao.update(entity);
    }

    public int delete(long merchantNo, String id) {
        UnitEntity entity = findById(merchantNo, id);
        if (entity == null) {
            throw new CustomException(ResultCode.BAD_REQUEST, "单位信息错误");
        }
        if (entity.getMerchantNo() == 0) {
            throw new CustomException(ResultCode.BAD_REQUEST, "系统预设单位信息不能删除");
        }
        SpuQuery spuQuery = new SpuQuery();
        spuQuery.setUnitId(id);
        List<SpuListVo> spus = spuService.list(merchantNo, spuQuery);
        if (!CollectionUtils.isEmpty(spus)) {
            throw new CustomException("无法删除使用中的单位信息");
        }
        return unitDao.delete(merchantNo, id);
    }

    public UnitEntity findById(long merchantNo, String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return unitDao.findById(merchantNo, id);
    }

    public UnitVo findByMerchantNoAndName(long merchantNo, String name) {
        UnitEntity entity = unitDao.findByMerchantNoAndName(merchantNo, name);
        if (entity == null) {
            return null;
        }
        UnitVo vo = new UnitVo();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    public List<UnitVo> findAllByIds(long merchantNo, List<String> ids) {
        List<UnitEntity> list = unitDao.findAllByIds(merchantNo, ids);
        return list.stream().map(x -> {
            UnitVo vo = new UnitVo();
            BeanUtils.copyProperties(x, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    public PageVo<UnitVo> page(long merchantNo,
                               String name,
                               UnitQuery query) {
        int total = unitDao.count(merchantNo, name);
        if (total <= 0) {
            return PageResult.of();
        }
        List<UnitEntity> list = unitDao.findPage(merchantNo, name, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<UnitVo> vos = list.stream().map(x -> {
            UnitVo vo = new UnitVo();
            BeanUtils.copyProperties(x, vo);
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }

    public List<UnitVo> findAll(long merchantNo) {
        List<UnitEntity> units = unitDao.findAll(merchantNo);
        if (!units.iterator().hasNext()) {
            return null;
        }
        return units.stream().map(x -> {
            UnitVo vo = new UnitVo();
            BeanUtils.copyProperties(x, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
