package com.mammon.cashier.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 优惠计算方式，1折扣，2积分抵扣，3优惠券抵扣，4加价减价，5抹零
 */
@Data
public class CashierRefundSkuComputeVo {
    /**
     * 销售订单productId
     */
    private String id;

    private String skuId;

    private String skuName;

    /**
     * 数量
     */
    private BigDecimal quantity;

    /**
     * 原单价
     */
    private BigDecimal referenceAmount;

    /**
     * 调整单价金额
     */
    private BigDecimal adjustAmount;

    /**
     * 应退
     */
    private BigDecimal payableAmount;

    /**
     * 标记是否剩余全退
     */
    private boolean allRefund;
}
