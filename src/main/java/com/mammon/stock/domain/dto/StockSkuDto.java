package com.mammon.stock.domain.dto;

import lombok.Data;

/**
 * 门店库存
 */
@Data
public class StockSkuDto {

    private String spuId;

    private String skuId;

    /**
     * 规格编码
     */
    private String skuCode;

    /**
     * 规格条码
     */
    private String skuNo;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * 进价
     */
    private long purchaseAmount;

    /**
     * 零售价(分)
     */
    private long referenceAmount;

    /**
     * 重量
     */
    private long skuWeight;

    /**
     * 库存
     */
    private long sellStock;

    /**
     * 状态
     */
    private int status;
}
