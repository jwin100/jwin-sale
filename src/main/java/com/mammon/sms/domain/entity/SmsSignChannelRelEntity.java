package com.mammon.sms.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 签名关联code
 */
@Data
public class SmsSignChannelRelEntity {

    private String id;

    private String signId;

    private String channelId;

    private String signCode;

    /**
     * 审核状态
     */
    private String status;

    private String errorDesc;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
