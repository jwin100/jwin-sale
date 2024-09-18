package com.mammon.goods.service;

import com.mammon.common.Generate;
import com.mammon.goods.channel.factory.BarcodeChannel;
import com.mammon.goods.channel.factory.BarcodeChannelFactory;
import com.mammon.goods.dao.GoodsDao;
import com.mammon.goods.domain.entity.GoodsEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class GoodsService {

    private static final String channelCode = "wanwei";

    @Resource
    private GoodsDao goodsDao;

    public GoodsEntity save(GoodsEntity entity) {
        entity.setId(Generate.generateUUID());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        goodsDao.save(entity);
        return entity;
    }

    public GoodsEntity findByBarcode(String barcode) {
        GoodsEntity goods = goodsDao.findByBarcode(barcode);
        if (goods != null) {
            return goods;
        }
        // 调用外部接口
        BarcodeChannel channel = BarcodeChannelFactory.get(channelCode);
        goods = channel.getBarcode(barcode);
        if (goods == null) {
            return null;
        }
        return save(goods);
    }
}
