package com.mammon.cashier.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CashierDiscountDto {

    private String id;

    private BigDecimal discount;
}
