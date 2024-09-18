package com.mammon.stock.service;

import com.mammon.common.Generate;
import com.mammon.stock.dao.StockInventoryProductDao;
import com.mammon.stock.domain.vo.StockInventoryProductVo;
import com.mammon.stock.domain.dto.StockInventoryProductDto;
import com.mammon.stock.domain.entity.StockInventoryProductEntity;
import com.mammon.utils.StockUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2024/4/1 16:26
 */
@Service
public class StockInventoryProductService {

    @Resource
    private StockInventoryProductDao stockInventoryProductDao;

    @Transactional(rollbackFor = Exception.class)
    public void batchCreate(String inventoryId, List<StockInventoryProductDto> dtos) {
        dtos.forEach(x -> {
            StockInventoryProductEntity entity = new StockInventoryProductEntity();
            entity.setId(Generate.generateUUID());
            entity.setInventoryId(inventoryId);
            entity.setSpuId(x.getSpuId());
            entity.setSpuName(x.getSpuName());
            entity.setCategoryId(x.getCategoryId());
            entity.setCategoryName(x.getCategoryName());
            entity.setBeforeStock(StockUtil.parse(x.getBeforeStock()));
            entity.setRealityStock(StockUtil.parse(x.getRealityStock()));
            entity.setPhaseStock(StockUtil.parse(x.getRealityStock()) - StockUtil.parse(x.getBeforeStock()));
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
            stockInventoryProductDao.save(entity);
        });
    }

    public List<StockInventoryProductVo> findAllByInventoryId(String inventoryId) {
        List<StockInventoryProductEntity> list = stockInventoryProductDao.findByInventoryId(inventoryId);
        return list.stream().map(x -> {
            StockInventoryProductVo vo = new StockInventoryProductVo();
            BeanUtils.copyProperties(x, vo);
            vo.setBeforeStock(StockUtil.parseBigDecimal(x.getBeforeStock()));
            vo.setRealityStock(StockUtil.parseBigDecimal(x.getRealityStock()));
            vo.setPhaseStock(StockUtil.parseBigDecimal(x.getPhaseStock()));
            return vo;
        }).collect(Collectors.toList());
    }
}
