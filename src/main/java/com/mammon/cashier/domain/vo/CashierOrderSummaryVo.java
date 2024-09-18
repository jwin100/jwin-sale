package com.mammon.cashier.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @since 2024/2/21 11:47
 */
@Data
public class CashierOrderSummaryVo {

    /**
     * 销售单数
     */
    private long orderTotal;

    /**
     * 销售金额
     */
    private BigDecimal orderAmount;
}
