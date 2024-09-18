package com.mammon.stock.service;

import com.mammon.common.Generate;
import com.mammon.stock.dao.StockClaimSkuDao;
import com.mammon.stock.domain.dto.StockClaimSkuDto;
import com.mammon.stock.domain.entity.StockClaimSkuEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @since 2024/3/12 15:10
 */
@Service
public class StockClaimSkuService {

    @Resource
    private StockClaimSkuDao stockClaimSkuDao;

    @Transactional(rollbackFor = Exception.class)
    public void batchSave(String splitId, List<StockClaimSkuDto> dtos) {
        dtos.forEach(dto -> {
            StockClaimSkuEntity entity = new StockClaimSkuEntity();
            BeanUtils.copyProperties(dto, entity);
            entity.setId(Generate.generateUUID());
            entity.setSplitId(splitId);
            stockClaimSkuDao.save(entity);
        });
    }

    public void deleteBySplitId(String splitId) {
        stockClaimSkuDao.deleteBySplitId(splitId);
    }

    public List<StockClaimSkuEntity> findAllBySplitId(String splitId) {
        return stockClaimSkuDao.findAllBySplitId(splitId);
    }
}
