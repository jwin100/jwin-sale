package com.mammon.cashier.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CashierRefundSkuDto {

    private String id;

    private String skuId;

    private BigDecimal quantity;

    /**
     * 调整单价金额
     */
    private BigDecimal adjustAmount;
}
