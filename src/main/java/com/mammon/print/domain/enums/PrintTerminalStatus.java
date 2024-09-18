package com.mammon.print.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/2/26 13:24
 */
@Getter
@AllArgsConstructor
public enum PrintTerminalStatus implements IEnum<PrintTerminalStatus> {
    ONLINE(1, "在线"),
    OFFLINE(2, "离线"),
    PAPER(3, "缺纸"),
    ;

    private final int code;
    private final String name;
}
