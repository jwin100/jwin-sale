package com.mammon.cashier.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单商品信息
 */
@Data
public class CashierOrderProductEntity {

    private String id;

    private String orderId;

    private String spuId;

    private String skuId;

    /**
     * skuName
     */
    private String skuName;

    private String picture;

    /**
     * 销售数量
     */
    private long saleQuantity;

    /**
     * 单价
     */
    private long referenceAmount;

    /**
     * 调整单价
     */
    private long adjustAmount;

    /**
     * 应收
     */
    private long payableAmount;

    /**
     * 分摊后金额(整单优惠分摊后)
     */
    private long divideAmount;

    /**
     * 已退数量
     */
    private long refundQuantity;

    /**
     * 已退金额
     */
    private long refundAmount;

    private long integral;

    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
