package com.mammon.auth.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/4/1 11:04
 */
@Getter
@AllArgsConstructor
public enum HandoverType implements IEnum<HandoverType> {
    RECEIVE(1, "接班"),
    HAND(2, "交班"),
    ;

    private final int code;
    private final String name;
}
