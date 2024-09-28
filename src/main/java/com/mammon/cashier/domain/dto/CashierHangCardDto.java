package com.mammon.cashier.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @since 2024/7/16 10:07
 */
@Data
public class CashierHangCardDto {


    /**
     * 临时商品没有id,为了区分多个临时商品，统一用key来标识购物车商品唯一
     */
    private String key;

    /**
     * 有skuId的情况，要保证skuId购物车唯一
     */
    private String skuId;

    private String skuName;

    private String picture;

    /**
     * 数量
     */
    private BigDecimal quantity;

    /**
     * 原单价
     */
    private BigDecimal referenceAmount;

    /**
     * 原应收（调整前价格）
     */
    private BigDecimal originAmount;

    /**
     * 调整单价金额
     */
    private BigDecimal adjustAmount;

    /**
     * 应收
     */
    private BigDecimal payableAmount;

    /**
     * 当前库存
     */
    private BigDecimal sellStock;

    /**
     * 单位
     */
    private String unitId;

    private String unitName;

    /**
     * 单位类型
     */
    private int unitType;

    private String unitTypeName;
}
