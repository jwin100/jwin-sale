package com.mammon.clerk.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2023/9/6 10:51
 */
@Getter
@AllArgsConstructor
public enum RoleType implements IEnum<RoleType> {
    MERCHANT(1, "商户权限"),
    STORE(2, "门店权限"),
    STAFF(3, "店员权限"),
    ;

    private final int code;
    private final String name;
}
