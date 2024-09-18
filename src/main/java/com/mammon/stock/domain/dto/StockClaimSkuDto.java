package com.mammon.stock.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/12 15:18
 */
@Data
public class StockClaimSkuDto {

    /**
     * 大件商品id
     */
    private String largeSpuId;

    /**
     * 大件商品规格id
     */
    private String largeSkuId;

    private String largeSkuNo;

    private String largeSkuCode;

    private String largeSkuName;

    private String smallSpuId;

    private String smallSkuId;

    private String smallSkuNo;

    private String smallSkuCode;

    private String smallSkuName;
}
