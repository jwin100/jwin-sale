package com.mammon.cashier.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CashierDiscountEntity {

    private String id;

    private long merchantNo;

    private long discount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
