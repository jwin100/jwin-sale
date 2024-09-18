package com.mammon.sms.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/5/8 10:00
 */
@Getter
@AllArgsConstructor
public enum SmsSendItemStatusEnum implements IEnum<SmsSendItemStatusEnum> {
    WAITING(0, "等待发送"),
    SENDING(1, "发送中"),
    SUCCESS(2, "发送成功"),
    FAIL(3, "发送失败");

    private final int code;
    private final String name;
}
