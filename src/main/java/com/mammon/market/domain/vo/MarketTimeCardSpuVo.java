package com.mammon.market.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @since 2023/11/15 15:03
 */
@Data
public class MarketTimeCardSpuVo {

    private String skuId;

    private String spuId;

    private String spuName;

    private String skuName;

    private BigDecimal sellStock;
}
