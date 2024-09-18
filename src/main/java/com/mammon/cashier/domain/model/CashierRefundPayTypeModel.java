package com.mammon.cashier.domain.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @date 2023-06-09 11:53:30
 */
@Data
public class CashierRefundPayTypeModel {

    /**
     * 支付方式
     */
    private int payCode;

    /**
     * 应退
     */
    private BigDecimal refundAmount;

    private String countedId;

    private long countedTotal;
}
