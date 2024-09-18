package com.mammon.stock.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @since 2024/3/14 11:43
 */
@Data
public class StockClaimSplitDto {

    /**
     * 大包商品
     */
    private String largeSpuId;

    private String largeSkuId;

    private String smallSpuId;

    private String smallSkuId;

    /**
     * 拆分数量
     */
    private BigDecimal splitQuantity;
}
