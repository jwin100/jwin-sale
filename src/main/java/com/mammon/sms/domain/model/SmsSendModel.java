package com.mammon.sms.domain.model;

import lombok.Data;

/**
 * @author dcl
 * @since 2023/8/21 11:45
 */
@Data
public class SmsSendModel {
    /**
     * 是否免费，1：免费
     */
    private int free;

    private int tempGroup;

    private int tempType;

    private int smsType;

    private String signId;

    private String tempId;

    private String content;

    private int messageCnt;

    private int messageChannelCnt;

    private int consumeCnt;

    private String channelId;

    private int sendStatus;

    private String errorDesc;
}
