package com.mammon.clerk.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/4/1 11:09
 */
@Getter
@AllArgsConstructor
public enum AccountLogType implements IEnum<AccountLogType> {
    LOGIN(1, "登录"),
    LOGOUT(2, "退出登录"),
    CLOSE_PAGE(3, "关闭页面"),
    ;

    private final int code;
    private final String name;
}
