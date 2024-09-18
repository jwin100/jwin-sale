package com.mammon.cashier.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @since 2023/7/18 15:30
 */
@Data
public class CashierOrderPayResultVo {

    private String orderId;

    /**
     * 应收
     */
    private BigDecimal payableAmount;

    /**
     * 已收
     */
    private BigDecimal receivedAmount;

    /**
     * 待收
     */
    private BigDecimal waitAmount;
}
