package com.mammon.sms.domain.entity;

import com.mammon.sms.enums.SmsTypeEnum;
import com.mammon.sms.enums.SmsTempTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 模板信息
 */
@Data
public class SmsTemplateEntity {

    private String id;

    private long merchantNo;

    private String tempName;

    /**
     * @see com.mammon.sms.enums.SmsTempGroupEnum
     */
    private int tempGroup;

    /**
     * 短信类型
     *
     * @see SmsTypeEnum
     */
    private int smsType;

    /**
     * 短信模板类型
     *
     * @see SmsTempTypeEnum
     */
    private int tempType;

    /**
     * 模板内容
     */
    private String template;

    /**
     * 1:默认使用(同一个商户，同一tempType下，只能有一个默认)
     */
    private int defaultStatus;

    private int status;

    private String errorDesc;

    /**
     * 审核人
     */
    private String operationId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
