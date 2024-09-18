package com.mammon.stock.domain.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/12 15:33
 */
@Data
public class StockClaimSkuVo {

    private String largeSpuId;

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
