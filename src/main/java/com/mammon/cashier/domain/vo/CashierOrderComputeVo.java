package com.mammon.cashier.domain.vo;

import com.mammon.cashier.domain.dto.CashierOrderSkuDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠计算方式，1折扣，2积分抵扣，3优惠券抵扣，4抹零，5加价减价
 */
@Data
public class CashierOrderComputeVo {
    /**
     * 原价(调整前金额)
     */
    private BigDecimal originalAmount;

    /**
     * 抹零类型
     */
    private int ignoreType;

    /**
     * 抹零金额
     */
    private BigDecimal ignoreAmount;

    /**
     * 折扣
     */
    private BigDecimal discount;

    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 优惠金额
     */
    private BigDecimal preferentialAmount;

    /**
     * 优惠后
     */
    private BigDecimal collectAmount;

    /**
     * 调整金额(手动加减价)
     */
    private BigDecimal adjustAmount;

    /**
     * 应收=原价-优惠金额
     */
    private BigDecimal payableAmount;

    /**
     * 1：商品销售，2：余额储值，3：计次开卡
     */
    private int category;

    /**
     * 商品件数
     */
    private long skuTotal;

    private List<CashierOrderSkuComputeVo> skus = new ArrayList<>();
}
