package com.mammon.stock.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门店库存
 */
@Data
public class StockSkuEntity {

    private String id;

    private String stockSpuId;

    private long merchantNo;

    private long storeNo;

    private String spuId;

    private String skuId;

    /**
     * 规格编码
     */
    private String skuCode;

    /**
     * 规格条码
     */
    private String skuNo;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * 进价
     */
    private long purchaseAmount;

    /**
     * 零售价(分)
     */
    private long referenceAmount;

    /**
     * 重量
     */
    private long skuWeight;

    /**
     * 库存
     */
    private long sellStock;

    private String joinSpec;

    /**
     * 商品状态(上架，下架)
     */
    private int status;

    /**
     * 是否删除（1:删除，0:未删除）
     * --子表删除的时候，判断是否剩最后一个，如果是，提示不能删除(必须通过spu删除)
     */
    private int deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
