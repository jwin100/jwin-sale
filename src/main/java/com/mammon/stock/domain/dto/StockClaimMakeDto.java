package com.mammon.stock.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @since 2024/3/14 11:43
 */
@Data
public class StockClaimMakeDto {

    private String smallSpuId;

    private String smallSkuId;

    /**
     * 大包商品
     */
    private String largeSpuId;

    private String largeSkuId;

    /**
     * 组装数量
     */
    private BigDecimal makeQuantity;
}
