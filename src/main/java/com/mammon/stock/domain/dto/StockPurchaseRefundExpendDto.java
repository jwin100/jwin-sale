package com.mammon.stock.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @date 2023-03-27 15:03:24
 */
@Data
public class StockPurchaseRefundExpendDto {

    private String purchaseOrderSkuId;

    private String skuId;

    private BigDecimal sellStock;
}
