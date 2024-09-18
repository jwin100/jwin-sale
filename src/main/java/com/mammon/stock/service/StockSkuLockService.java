package com.mammon.stock.service;

import com.mammon.common.Generate;
import com.mammon.stock.dao.StockSkuLockDao;
import com.mammon.stock.domain.vo.StockChangeVo;
import com.mammon.stock.domain.dto.StockStoreLockDto;
import com.mammon.stock.domain.entity.StockSkuLockEntity;
import com.mammon.utils.StockUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-06-08 15:44:59
 */
@Service
public class StockSkuLockService {
    
    @Resource
    private StockSkuLockDao stockSkuLockDao;

    @Resource
    private StockSkuService stockSkuService;

    /**
     * 锁定库存
     */
    public StockChangeVo lockStock(StockStoreLockDto dto) {
        StockChangeVo vo = stockSkuService.editSellStockExpend(dto.getMerchantNo(), dto.getStoreNo(), dto.getSkuId(), dto.getLockStock());
        if (vo == null) {
            return vo;
        }
        StockSkuLockEntity entity = new StockSkuLockEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setCreateTime(LocalDateTime.now());
        stockSkuLockDao.save(entity);
        return StockChangeVo.builder().code(1).build();
    }

    /**
     * 释放库存
     */
    public void unLockStock(StockStoreLockDto dto) {
        StockSkuLockEntity entity = findByOrderId(dto.getMerchantNo(), dto.getStoreNo(), dto.getOrderId(), dto.getSkuId());
        if (entity != null) {
            stockSkuLockDao.delete(entity.getId());
        }
    }

    /**
     * 回滚库存
     */
    @Transactional(rollbackFor = Exception.class)
    public void callbackStock(StockStoreLockDto dto) {
        StockSkuLockEntity entity = findByOrderId(dto.getMerchantNo(), dto.getStoreNo(), dto.getOrderId(), dto.getSkuId());
        if (entity != null) {
            stockSkuLockDao.delete(entity.getId());
            stockSkuService.editSellStockReplenish(entity.getMerchantNo(), entity.getStoreNo(), entity.getSkuId(), StockUtil.parseBigDecimal(entity.getLockStock()));
        }
    }

    public StockSkuLockEntity findByOrderId(long merchantNo, long storeNo, String orderNo, String skuId) {
        return stockSkuLockDao.findByOrderId(merchantNo, storeNo, orderNo, skuId);
    }
}
