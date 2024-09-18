package com.mammon.sms.domain.entity;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/5/9 13:41
 */
@Data
public class SmsTemplateSettingEntity {

    private String id;

    private long merchantNo;

    /**
     * 模板类型
     */
    private int tempType;

    /**
     * 通知默认模板
     */
    private String tempId;

    /**
     * 是否开启自动通知
     */
    private int status;
}
