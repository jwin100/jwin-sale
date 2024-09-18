package com.mammon.cashier.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CashierDiscountVo {

    private String id;

    private long merchantNo;

    private BigDecimal discount;

    private String discountName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
