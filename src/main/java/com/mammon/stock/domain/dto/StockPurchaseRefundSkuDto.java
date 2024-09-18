package com.mammon.stock.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockPurchaseRefundSkuDto {

    private String spuId;

    private String skuId;

    private BigDecimal refundQuantity;

    /**
     * 退货单价
     */
    private BigDecimal refundAmount;
}
