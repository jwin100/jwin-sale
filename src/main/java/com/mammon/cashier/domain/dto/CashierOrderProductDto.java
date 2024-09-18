package com.mammon.cashier.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单商品信息
 */
@Data
public class CashierOrderProductDto {

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
    private BigDecimal saleQuantity;

    /**
     * 单价
     */
    private BigDecimal referenceAmount;

    /**
     * 调整单价
     */
    private BigDecimal adjustAmount;

    /**
     * 应收
     */
    private BigDecimal payableAmount;

    /**
     * 分摊后金额(整单优惠分摊后)
     */
    private BigDecimal divideAmount;

    private long integral;

    /**
     * 是否服务商品
     */
    private int countedType;
}
