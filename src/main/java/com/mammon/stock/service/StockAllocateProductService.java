package com.mammon.stock.service;

import com.mammon.common.Generate;
import com.mammon.stock.dao.StockAllocateProductDao;
import com.mammon.stock.domain.dto.StockAllocateSkuDto;
import com.mammon.stock.domain.entity.StockAllocateProductEntity;
import com.mammon.utils.StockUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockAllocateProductService {

    @Resource
    private StockAllocateProductDao stockAllocateProductDao;
    @Autowired
    private StockSpuService stockSpuService;

    @Transactional(rollbackFor = Exception.class)
    public void batchCreate(String allocateNo, List<StockAllocateSkuDto> dtos) {
        dtos.forEach(x -> {
            StockAllocateProductEntity entity = new StockAllocateProductEntity();
            BeanUtils.copyProperties(x, entity);
            entity.setAllocateNo(allocateNo);
            entity.setAllocateQuantity(StockUtil.parse(x.getAllocateQuantity()));
            entity.setOutQuantity(StockUtil.parse(x.getOutQuantity()));
            entity.setInQuantity(StockUtil.parse(x.getInQuantity()));
            create(entity);
        });
    }

    public void create(StockAllocateProductEntity entity) {
        entity.setId(Generate.generateUUID());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        stockAllocateProductDao.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchEdit(String allocateNo, List<StockAllocateSkuDto> dtos) {
        List<StockAllocateProductEntity> products = findAllByAllocateNo(allocateNo);
        dtos.forEach(x -> {
            if (StringUtils.isEmpty(x.getId())) {
                StockAllocateProductEntity entity = new StockAllocateProductEntity();
                BeanUtils.copyProperties(x, entity);
                entity.setAllocateNo(allocateNo);
                entity.setAllocateQuantity(StockUtil.parse(x.getAllocateQuantity()));
                entity.setOutQuantity(StockUtil.parse(x.getOutQuantity()));
                entity.setInQuantity(StockUtil.parse(x.getInQuantity()));
                create(entity);
            } else {
                StockAllocateProductEntity product = products.stream().filter(y -> y.getId().equals(x.getId())).findFirst().orElse(null);
                if (product == null) {
                    delete(x.getId());
                } else {
                    BeanUtils.copyProperties(x, product);
                    product.setAllocateQuantity(StockUtil.parse(x.getAllocateQuantity()));
                    product.setOutQuantity(StockUtil.parse(x.getOutQuantity()));
                    product.setInQuantity(StockUtil.parse(x.getInQuantity()));
                    edit(product);
                }
            }
        });
    }

    public void edit(StockAllocateProductEntity entity) {
        entity.setUpdateTime(LocalDateTime.now());
        stockAllocateProductDao.edit(entity);
    }

    public int delete(String id) {
        return stockAllocateProductDao.delete(id);
    }

    public int outStock(String id, long outQuantity) {
        return stockAllocateProductDao.outStock(id, outQuantity);
    }

    public int inStock(String id, long inQuantity) {
        return stockAllocateProductDao.inStock(id, inQuantity);
    }

    public List<StockAllocateProductEntity> findAllByAllocateNo(String allocateNo) {
        return stockAllocateProductDao.findAllByAllocateNo(allocateNo);
    }
}
