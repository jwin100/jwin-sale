package com.mammon.sms.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/5/8 10:10
 */
@Getter
@AllArgsConstructor
public enum SmsSignStatusEnum implements IEnum<SmsSignStatusEnum> {

    WAITING(0, "待审核"),
    SUCCESS(1, "审核通过"),
    FAIL(2, "审核失败");

    private final int code;
    private final String name;
}
