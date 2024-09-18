package com.mammon.stock.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-03-27 16:05:59
 */
@Data
public class StockPurchaseRefundSkuEntity {
    private String id;

    private String refundId;

    private String spuId;

    private String skuId;

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
