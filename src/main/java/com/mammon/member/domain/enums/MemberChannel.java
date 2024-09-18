package com.mammon.member.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/7/31 13:34
 */
@Getter
@AllArgsConstructor
public enum MemberChannel implements IEnum<MemberChannel> {
    MANUAL(1, "人工录入"),
    AUTONOMY(2, "自动注册"),
    ;

    private final int code;
    private final String name;
}
