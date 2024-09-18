package com.mammon.sms.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/5/8 10:14
 */
@Getter
@AllArgsConstructor
public enum SmsTempGroupEnum implements IEnum<SmsTempGroupEnum> {
    SYSTEM_SMS(1, "系统短信"),
    MERCHANT_SMS(2, "商户短信"),
    ;

    private final int code;
    private final String name;
}
