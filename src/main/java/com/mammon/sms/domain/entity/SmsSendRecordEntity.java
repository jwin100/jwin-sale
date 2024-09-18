package com.mammon.sms.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 短信发送前记录
 *
 * @author dcl
 * @since 2024/6/4 17:53
 */
@Data
public class SmsSendRecordEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String sendId;

    private String refId;

    /**
     * 短信类型
     */
    private int type;

    /**
     * 1：提交成功，2：提交失败
     */
    private int status;

    /**
     * 失败描述
     */
    private String errorDesc;

    private LocalDateTime createTime;
}
