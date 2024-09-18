package com.mammon.stock.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockPurchaseOrderSkuDto {

    private String spuId;

    private String skuId;

    /**
     * 采购数量
     */
    private BigDecimal purchaseQuantity;

    /**
     * 采购价格
     */
    private BigDecimal purchaseAmount;
}
