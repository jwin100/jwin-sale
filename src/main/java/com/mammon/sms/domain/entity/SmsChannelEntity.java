package com.mammon.sms.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SmsChannelEntity {
    private String id;

    private String channelCode;

    private String channelName;

    /**
     * 配置文件信息(分类放这里,这里放成list)
     */
    private String configStr;

    private int smsType;

    private int status;

    private int defaultStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
