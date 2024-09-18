package com.mammon.stock.service;

import com.mammon.common.Generate;
import com.mammon.exception.CustomException;
import com.mammon.stock.dao.StockPurchaseRefundSkuDao;
import com.mammon.stock.domain.dto.StockPurchaseRefundSkuDto;
import com.mammon.stock.domain.entity.StockPurchaseRefundSkuEntity;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.StockUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dcl
 * @date 2023-03-27 16:09:08
 */
@Service
public class StockPurchaseRefundSkuService {

    @Resource
    private StockPurchaseRefundSkuDao stockPurchaseRefundSkuDao;

    @Transactional(rollbackFor = CustomException.class)
    public void batchSave(String refundId, List<StockPurchaseRefundSkuDto> dtos) {
        dtos.forEach(x -> {
            StockPurchaseRefundSkuEntity entity = new StockPurchaseRefundSkuEntity();
            BeanUtils.copyProperties(x, entity);
            entity.setRefundId(refundId);
            entity.setRefundQuantity(StockUtil.parse(x.getRefundQuantity()));
            entity.setRefundAmount(AmountUtil.parse(x.getRefundAmount()));
            save(entity);
        });
    }

    public void save(StockPurchaseRefundSkuEntity entity) {
        entity.setId(Generate.generateUUID());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        stockPurchaseRefundSkuDao.save(entity);
    }

    public void deleteAllByRefundId(String refundId) {
        stockPurchaseRefundSkuDao.deleteAllByRefundId(refundId);
    }

    public List<StockPurchaseRefundSkuEntity> findAllByRefundId(String refundId) {
        return stockPurchaseRefundSkuDao.findAllByRefundId(refundId);
    }
}
