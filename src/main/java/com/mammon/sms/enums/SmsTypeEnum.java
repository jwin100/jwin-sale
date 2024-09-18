package com.mammon.sms.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信类型
 *
 * @author dcl
 * @since 2024/5/8 10:20
 */
@Getter
@AllArgsConstructor
public enum SmsTypeEnum implements IEnum<SmsTypeEnum> {
    CAPTCHA(1, "验证码"),
    NOTICE(2, "通知"),
    MARKET(3, "营销"),
    ;

    private final int code;
    private final String name;
}
