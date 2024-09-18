package com.mammon.cashier.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CashierRefundProductEntity {

    private String id;

    private String refundId;

    private String orderProductId;

    private String spuId;

    private String skuId;

    /**
     * 商品名
     */
    private String skuName;

    /**
     * 商品图
     */
    private String picture;

    /**
     * 退货数量
     */
    private long refundQuantity;

    /**
     * 原单价
     */
    private long referenceAmount;

    /**
     * 调整单价金额
     */
    private long adjustAmount;

    /**
     * 应退
     */
    private long payableAmount;

    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
