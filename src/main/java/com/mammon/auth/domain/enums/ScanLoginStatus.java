package com.mammon.auth.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/5/23 17:02
 */
@Getter
@AllArgsConstructor
public enum ScanLoginStatus implements IEnum<ScanLoginStatus> {
    LOGIN_SUCCESS(1, "登录成功"),
    ;
    private final int code;
    private final String name;
}
