package com.mammon.stock.service;

import com.mammon.common.Generate;
import com.mammon.exception.CustomException;
import com.mammon.stock.dao.StockPurchaseOrderSkuDao;
import com.mammon.stock.domain.dto.StockPurchaseOrderSkuDto;
import com.mammon.stock.domain.dto.StockStoreReplenishDto;
import com.mammon.stock.domain.dto.StockPurchaseRefundExpendDto;
import com.mammon.stock.domain.entity.StockPurchaseOrderSkuEntity;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.StockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class StockPurchaseOrderSkuService {

    @Resource
    private StockPurchaseOrderSkuDao stockPurchaseOrderSkuDao;

    @Transactional(rollbackFor = CustomException.class)
    public void batchSave(String purchaseId, List<StockPurchaseOrderSkuDto> dtos) {
        dtos.forEach(x -> {
            StockPurchaseOrderSkuEntity entity = new StockPurchaseOrderSkuEntity();
            BeanUtils.copyProperties(x, entity);
            entity.setPurchaseId(purchaseId);
            entity.setPurchaseQuantity(StockUtil.parse(x.getPurchaseQuantity()));
            entity.setPurchaseAmount(AmountUtil.parse(x.getPurchaseAmount()));
            save(entity);
        });
    }

    /**
     * 批量入库
     *
     * @param purchaseId
     * @param replenishDtos
     */
    public void batchReplenish(String purchaseId, List<StockStoreReplenishDto> replenishDtos) {
        replenishDtos.forEach(x -> {
            StockPurchaseOrderSkuEntity entity = new StockPurchaseOrderSkuEntity();
            entity.setPurchaseId(purchaseId);
            entity.setId(x.getId());
            entity.setReplenishQuantity(StockUtil.parse(x.getSellStock()));
            editReplenish(entity);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchRefund(String purchaseId, List<StockPurchaseRefundExpendDto> refundDtos) {
        refundDtos.forEach(x -> {
            StockPurchaseOrderSkuEntity entity = new StockPurchaseOrderSkuEntity();
            entity.setId(x.getPurchaseOrderSkuId());
            entity.setPurchaseId(purchaseId);
            entity.setRefundQuantity(StockUtil.parse(x.getSellStock()));
            editRefund(entity);
        });
    }

    public void save(StockPurchaseOrderSkuEntity entity) {
        entity.setId(Generate.generateUUID());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setReplenishQuantity(0);
        stockPurchaseOrderSkuDao.save(entity);
    }

    public void editReplenish(StockPurchaseOrderSkuEntity entity) {
        entity.setUpdateTime(LocalDateTime.now());
        stockPurchaseOrderSkuDao.editReplenish(entity);
    }

    public void editRefund(StockPurchaseOrderSkuEntity entity) {
        entity.setUpdateTime(LocalDateTime.now());
        stockPurchaseOrderSkuDao.editRefund(entity);
    }

    public void deleteAllByPurchaseId(String purchaseId) {
        stockPurchaseOrderSkuDao.deleteAllByPurchaseNo(purchaseId);
    }

    public List<StockPurchaseOrderSkuEntity> findAllByPurchaseId(String purchaseId) {
        return stockPurchaseOrderSkuDao.findAllByPurchaseId(purchaseId);
    }
}
