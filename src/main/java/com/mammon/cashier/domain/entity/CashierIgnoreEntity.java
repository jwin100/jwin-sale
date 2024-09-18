package com.mammon.cashier.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CashierIgnoreEntity {

    private String id;

    private long merchantNo;

    /**
     * 抹零类型，0:不抹零，1:抹分，2:抹角
     */
    private int type;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
