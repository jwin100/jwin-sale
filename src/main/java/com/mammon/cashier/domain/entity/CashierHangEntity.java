package com.mammon.cashier.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 挂单
 */
@Data
public class CashierHangEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String name;

    private String hangNo;

    private String detail;

    private int status;

    private String remark;

    private String operationId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
