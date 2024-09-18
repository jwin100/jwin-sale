package com.mammon.sms.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dcl
 * @since 2023/12/11 16:49
 */
@Getter
@AllArgsConstructor
public enum SmsSendStatusEnum implements IEnum<SmsSendStatusEnum> {
    SUBMIT(0, "提交中"),
    SUBMIT_ERROR(1, "提交失败"),
    EXAMINE_WAIT(2, "待审核"),
    EXAMINE_FAIL(3, "审核驳回"),
    SENDING(4, "发送中"),
    SEND_SUCCESS(5, "发送成功"),
    SEND_FAIL(6, "发送失败");

    private final int code;
    private final String name;
}
