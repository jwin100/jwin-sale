package com.mammon.cashier.domain.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 分摊优惠金额
 */
@Data
public class DivideAmountModel {

    private String id;

    /**
     * 权重
     */
    private BigDecimal weight;

    /**
     * 分摊优惠金额
     */
    private BigDecimal shareAmount;
}
