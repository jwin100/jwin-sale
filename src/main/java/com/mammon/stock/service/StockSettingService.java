package com.mammon.stock.service;

import com.mammon.enums.CommonStatus;
import com.mammon.stock.dao.StockSettingDao;
import com.mammon.stock.domain.dto.StockSettingDto;
import com.mammon.stock.domain.entity.StockSettingEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2023/12/27 10:36
 */
@Service
public class StockSettingService {

    @Resource
    private StockSettingDao stockSettingDao;

    public StockSettingEntity save(long merchantNo, StockSettingDto dto) {
        StockSettingEntity entity = new StockSettingEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setMerchantNo(merchantNo);
        stockSettingDao.save(entity);
        return entity;
    }

    public void modify(long merchantNo, StockSettingDto dto) {
        StockSettingEntity entity = stockSettingDao.findByMerchantNo(merchantNo);
        if (entity == null) {
            return;
        }
        BeanUtils.copyProperties(dto, entity);
        stockSettingDao.update(entity);
    }

    public StockSettingEntity findByMerchantNo(long merchantNo) {
        StockSettingEntity entity = stockSettingDao.findByMerchantNo(merchantNo);
        if (entity != null) {
            return entity;
        }
        StockSettingDto dto = new StockSettingDto();
        dto.setPurchaseOrderExamine(CommonStatus.DISABLED.getCode());
        dto.setPurchaseRefundExamine(CommonStatus.DISABLED.getCode());
        dto.setAllocateExamine(CommonStatus.DISABLED.getCode());
        dto.setClaim(CommonStatus.DISABLED.getCode());
        return save(merchantNo, dto);
    }
}
