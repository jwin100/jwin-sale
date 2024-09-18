package com.mammon.stock.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 采购单商品信息
 */
@Data
public class StockPurchaseOrderSkuEntity {

    private String id;

    private String purchaseId;

    private String spuId;

    private String skuId;

    /**
     * 采购数量
     */
    private long purchaseQuantity;

    /**
     * 采购价格
     */
    private long purchaseAmount;

    /**
     * 入库数量
     */
    private long replenishQuantity;

    /**
     * 退货数量
     */
    private long refundQuantity;

    /**
     * 退货价格
     */
    private long refundAmount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
