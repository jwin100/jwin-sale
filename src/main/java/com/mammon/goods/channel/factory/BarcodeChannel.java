package com.mammon.goods.channel.factory;

import com.mammon.goods.domain.entity.GoodsEntity;

/**
 * @author dcl
 * @since 2024/4/11 18:21
 */
public interface BarcodeChannel {

    GoodsEntity getBarcode(String code);
}
