package com.mammon.cashier.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CashierOrderYearTotalVo {

    private int month;

    private BigDecimal total;
}
