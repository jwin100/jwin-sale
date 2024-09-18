package com.mammon.stock.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 采购入库
 * @author dcl
 * @since 2023/12/18 19:18
 */
@Data
public class StockPurchaseReplenishDto {

    /**
     * sku
     */
    private String skuId;

    /**
     * 入库数量
     */
    private BigDecimal replenishQuantity;
}
