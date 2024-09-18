package com.mammon.cashier.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 优惠计算方式，1折扣，2积分抵扣，3优惠券抵扣，4加价减价，5抹零
 */
@Data
public class CashierOrderSkuComputeVo {
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
     * 商品库存
     */
    private BigDecimal sellStock;

    /**
     * 单位id
     */
    private String unitId;

    /**
     * 单位名
     */
    private String unitName;

    /**
     * 单位类型
     */
    private int unitType;

    /**
     * 单位类型
     */
    private String unitTypeName;

    /**
     * 商品图
     */
    private String picture;
}
