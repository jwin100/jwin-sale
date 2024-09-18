package com.mammon.cashier.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @date 2023-06-09 11:53:30
 */
@Data
public class CashierRefundPayTypeDto {

    /**
     * 支付方式
     */
    private int payCode;

    /**
     * 应退
     */
    private BigDecimal refundAmount;
}
