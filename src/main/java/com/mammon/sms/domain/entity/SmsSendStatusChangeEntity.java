package com.mammon.sms.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SmsSendStatusChangeEntity {

    private String id;

    private String sendId;

    private int beforeStatus;

    private int changeStatus;

    private int afterStatus;

    /**
     * 变更人(null:系统变更)
     */
    private String operationId;

    /**
     * 变更日期
     */
    private LocalDateTime changeTime;
}
