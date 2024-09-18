package com.mammon.stock.service;

import com.mammon.common.Generate;
import com.mammon.exception.CustomException;
import com.mammon.stock.dao.StockRecordProductDao;
import com.mammon.stock.domain.dto.StockRecordSkuDto;
import com.mammon.stock.domain.entity.StockRecordSkuEntity;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.StockUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockRecordSkuService {

    @Resource
    private StockRecordProductDao stockRecordProductDao;

    @Transactional(rollbackFor = CustomException.class)
    public void batchCreate(long merchantNo, long storeNo, String accountId, String recordNo, List<StockRecordSkuDto> products) {
        products.forEach(x -> {
            create(merchantNo, storeNo, accountId, recordNo, x);
        });
    }

    public int create(long merchantNo, long storeNo, String accountId, String recordNo, StockRecordSkuDto dto) {
        StockRecordSkuEntity entity = new StockRecordSkuEntity();
        entity.setId(Generate.generateUUID());
        entity.setRecordNo(recordNo);
        entity.setSpuId(dto.getSpuId());
        entity.setSkuId(dto.getSkuId());
        entity.setSkuName(dto.getSkuName());
        entity.setRecordAmount(AmountUtil.parse(dto.getRecordAmount()));
        entity.setRecordQuantity(StockUtil.parse(dto.getRecordQuantity()));
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return stockRecordProductDao.save(entity);
    }

    public List<StockRecordSkuEntity> findAllByRecordNo(String recordNo) {
        return stockRecordProductDao.findAllByRecordNo(recordNo);
    }
}
