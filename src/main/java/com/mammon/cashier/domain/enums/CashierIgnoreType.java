package com.mammon.cashier.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2023/11/9 16:08
 */
@Getter
@AllArgsConstructor
public enum CashierIgnoreType implements IEnum<CashierIgnoreType> {
    NOT_IGNORE(0, "不抹零"),
    BRANCH(1, "抹分"),
    HORN(2, "抹角"),
    ELEMENT(2, "元取整"),
    ;

    private final int code;
    private final String name;
}
