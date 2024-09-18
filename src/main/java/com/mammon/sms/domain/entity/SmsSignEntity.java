package com.mammon.sms.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 签名信息
 */
@Data
public class SmsSignEntity {

    private String id;

    private long merchantNo;

    /**
     * 签名
     */
    private String signName;

    /**
     * 1：默认使用
     */
    private int defaultStatus;

    /**
     * 状态
     */
    private int status;

    private String errorDesc;

    /**
     * 审核人
     */
    private String operationId;

    private LocalDateTime operationTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
