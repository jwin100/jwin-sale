package com.mammon.auth.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/5/30 9:56
 */
@Getter
@AllArgsConstructor
public enum WechatLoginStatus implements IEnum<WechatLoginStatus> {
    BING_PHONE(1, "待绑定手机号"),
    LOGIN_SUCCESS(2, "登录成功"),
    ;

    private final int code;
    private final String name;
}
