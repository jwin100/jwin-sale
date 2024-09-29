package com.mammon.clerk.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/9/28 22:49
 */
@Getter
@AllArgsConstructor
public enum AccountCashMode implements IEnum<AccountCashMode> {
    LIST_MODE(1,"列表模式"),
    SCAN_MODE(2,"扫描模式"),
    ;

    private final int code;
    private final String name;
}
