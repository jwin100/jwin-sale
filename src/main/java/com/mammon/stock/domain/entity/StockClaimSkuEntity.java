package com.mammon.stock.domain.entity;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/12 14:51
 */
@Data
public class StockClaimSkuEntity {

    private String id;

    private String splitId;

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
