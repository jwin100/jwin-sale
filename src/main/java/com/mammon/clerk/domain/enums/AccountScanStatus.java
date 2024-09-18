package com.mammon.clerk.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/5/23 11:45
 */
@Getter
@AllArgsConstructor
public enum AccountScanStatus implements IEnum<AccountScanStatus> {
    CREATED(1, "创建"),
    SCANNED(2, "已扫码"),
    EXPIRED(3, "已过期"),
    LOGIN(4, "登录成功"),
    CANCEL(5, "取消登录"),
    ;

    private final int code;
    private final String name;
}
