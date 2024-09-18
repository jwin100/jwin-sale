package com.mammon.stock.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockAllocateSkuDto {

    private String id;

    private String allocateNo;

    private String spuId;

    private String skuId;

    /**
     * 商品名
     */
    private String skuName;

    private String unitId;
    private String unitName;

    /**
     * 调拨数量
     */
    private BigDecimal allocateQuantity;

    /**
     * 实际出库数量
     */
    private BigDecimal outQuantity;

    /**
     * 实际入库数量
     */
    private BigDecimal inQuantity;
}
