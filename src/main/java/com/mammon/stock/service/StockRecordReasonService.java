package com.mammon.stock.service;

import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.stock.dao.StockRecordReasonDao;
import com.mammon.stock.domain.vo.StockRecordReasonVo;
import com.mammon.stock.domain.dto.StockRecordReasonDto;
import com.mammon.stock.domain.query.StockRecordReasonQuery;
import com.mammon.stock.domain.entity.StockRecordReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockRecordReasonService {

    @Resource
    private StockRecordReasonDao stockRecordReasonDao;

    public int create(long merchantNo, StockRecordReasonDto dto) {
        StockRecordReasonEntity entity = new StockRecordReasonEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setReasonName(dto.getReasonName());
        entity.setIoType(dto.getIoType());
        entity.setRemark(dto.getRemark());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return stockRecordReasonDao.save(entity);
    }

    public int update(long merchantNo, String id, StockRecordReasonDto dto) {
        return stockRecordReasonDao.update(merchantNo, id, dto.getReasonName(), dto.getIoType(), dto.getRemark());
    }

    public int delete(long merchantNo, String id) {
        return stockRecordReasonDao.delete(merchantNo, id);
    }

    public StockRecordReasonVo findById(long merchantNo, String id) {
        StockRecordReasonEntity entity = stockRecordReasonDao.findById(merchantNo, id);
        if (entity == null) {
            return null;
        }
        StockRecordReasonVo vo = new StockRecordReasonVo();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    public List<StockRecordReasonVo> findAll(long merchantNo) {
        List<StockRecordReasonEntity> list = stockRecordReasonDao.findAll(merchantNo);
        return list.stream().map(x -> {
            StockRecordReasonVo vo = new StockRecordReasonVo();
            BeanUtils.copyProperties(x, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    public PageVo<StockRecordReasonVo> page(long merchantNo, long storeNo, String accountId, StockRecordReasonQuery dto) {
        int total = stockRecordReasonDao.count(merchantNo, storeNo, accountId, dto);
        if (total <= 0) {
            return null;
        }
        List<StockRecordReasonEntity> list = stockRecordReasonDao.page(merchantNo, storeNo, accountId, dto);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<StockRecordReasonVo> vos = list.stream().map(x -> {
            StockRecordReasonVo vo = new StockRecordReasonVo();
            BeanUtils.copyProperties(x, vo);
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(dto.getPageIndex(), dto.getPageSize(), total, vos);
    }
}
