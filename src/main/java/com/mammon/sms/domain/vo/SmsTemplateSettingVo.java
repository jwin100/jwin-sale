package com.mammon.sms.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.sms.enums.SmsTypeEnum;
import com.mammon.sms.enums.SmsTempTypeEnum;
import lombok.Data;

/**
 * @author dcl
 * @since 2024/5/9 14:01
 */
@Data
public class SmsTemplateSettingVo {

    private String id;

    private long merchantNo;

    private String tempId;

    private String tempName;

    /**
     * @see com.mammon.sms.enums.SmsTempGroupEnum
     */
    private int tempGroup;

    /**
     * 模板分类
     *
     * @see SmsTypeEnum
     */
    private int smsType;

    /**
     * 模板类型
     *
     * @see SmsTempTypeEnum
     */
    private int tempType;

    private String tempTypeName;

    /**
     * 模板内容
     */
    private String template;

    /**
     * 是否启用自动通知
     */
    private int status;

    public String getTempTypeName() {
        return IEnum.getNameByCode(this.getTempType(), SmsTempTypeEnum.class);
    }
}
