package com.mammon.stock.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockRecordSkuDto {

    private String spuId;

    private String skuId;

    private String skuName;

    /**
     * 操作数量
     */
    private BigDecimal recordQuantity;

    /**
     * 操作价格
     */
    private BigDecimal recordAmount;
}
