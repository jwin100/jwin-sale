package com.mammon.cashier.domain.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/6/3 11:13
 */
@Data
public class CashierOrderProductInitVo {

    private String spuId;

    private String skuId;

    private String skuName;

    private String picture;

    private long integral;

    /**
     * 是否服务商品
     */
    private int countedType;
}
