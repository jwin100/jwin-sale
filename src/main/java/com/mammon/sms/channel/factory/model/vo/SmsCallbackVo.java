package com.mammon.sms.channel.factory.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2023/12/11 15:59
 */
@Data
public class SmsCallbackVo {

    private String phone;

    private String smsId;

    private int status;

    /**
     * 失败码
     */
    private String errCode;

    /**
     * 失败描述
     */
    private String errMsg;

    /**
     * 送达时间
     */
    private LocalDateTime sendTime;
}
