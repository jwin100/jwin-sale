package com.mammon.sms.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/5/9 13:55
 */
@Data
public class SmsTemplateSettingDto {

    /**
     * 默认使用模板
     */
    private String tempId;

    /**
     * 是否自动发送
     *
     * @see com.mammon.enums.CommonStatus
     */
    private int status;
}
