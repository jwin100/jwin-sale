package com.mammon.sms.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 模板关联渠道信息
 */
@Data
public class SmsTemplateChannelRelEntity {

    private String id;

    private String tempId;

    private String channelId;

    private String tempCode;

    /**
     * 通道审核状态
     */
    private String status;

    private String errorDesc;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
