package com.mammon.sms.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SmsEntity {

    private String id;

    private long merchantNo;

    /**
     * 余额
     */
    private long recharge;

    /**
     * 开通状态
     */
    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
