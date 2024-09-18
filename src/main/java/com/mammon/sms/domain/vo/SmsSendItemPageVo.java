package com.mammon.sms.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SmsSendItemPageVo {

    private String id;

    private String sendId;

    private String phone;

    private String userId;

    private String userName;

    /**
     * 发送内容
     */
    private String content;

    /**
     * @see com.mammon.sms.enums.SmsSendItemStatusEnum
     */
    private int status;

    private String errorDesc;

    private LocalDateTime createTime;
}
