package com.mammon.print.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/4/29 10:11
 */
@Getter
@AllArgsConstructor
public enum PrintRecordStatus implements IEnum<PrintTerminalStatus> {
    WAITING(1, "等待打印"),
    SUBMITTED(2, "已提交"),
    SUBMIT_FAIL(3, "提交失败"),
    SUCCESS(4, "打印成功"),
    FAIL(5, "打印失败"),
    ;

    private final int code;
    private final String name;
}
