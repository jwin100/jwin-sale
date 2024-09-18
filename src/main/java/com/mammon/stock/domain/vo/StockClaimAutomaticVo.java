package com.mammon.stock.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @since 2024/3/20 15:20
 */
@Data
public class StockClaimAutomaticVo {

    /**
     * 1:组装，2:拆包
     */
    private int type;

    private String largeSpuId;

    private String largeSkuId;

    private String largeSkuName;

    private String largeUnitId;

    private String largeUnitName;

    private BigDecimal largeQuantity;

    private String smallSpuId;

    private String smallSkuId;

    private String smallSkuName;

    private String smallUnitId;

    private String smallUnitName;

    private BigDecimal smallQuantity;
}
