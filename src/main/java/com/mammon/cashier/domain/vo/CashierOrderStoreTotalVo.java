package com.mammon.cashier.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CashierOrderStoreTotalVo {

    private long storeNo;

    private String storeName;

    private BigDecimal total;
}
