package com.mammon.cashier.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CashierOrderSkuDto {

    /**
     * 临时商品没有id,为了区分多个临时商品，统一用key来标识购物车商品唯一
     */
    private String key;

    /**
     * 有skuId的情况，要保证skuId购物车唯一
     */
    private String skuId;

    private String skuName;

    /**
     * 数量
     */
    private BigDecimal quantity;

    /**
     * 调整单价金额
     */
    private BigDecimal adjustAmount;

    private BigDecimal sellStock;

    /**
     * 商品图
     */
    private String picture;

    private int countedType;
}