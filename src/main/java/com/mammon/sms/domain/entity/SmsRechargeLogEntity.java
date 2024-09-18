package com.mammon.sms.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Recharge变更日志
 */
@Data
public class SmsRechargeLogEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    private long changeBefore;

    private long changeIn;

    private long changeAfter;

    private int changeStatus;

    /**
     * 0:减扣,1:增加
     */
    private int changeType;

    private String changeTypeDesc;

    /**
     * 关联sendId方便定位
     */
    private String sendId;

    private String orderNo;

    private String remark;

    private LocalDateTime createTime;
}
