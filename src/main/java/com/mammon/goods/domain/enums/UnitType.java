package com.mammon.goods.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/1/17 9:55
 */
@Getter
@AllArgsConstructor
public enum UnitType implements IEnum<UnitType> {
    NUMBER(0, "计数"),
    WEIGHT(1, "计重"),
    ;

    private final int code;
    private final String name;
}
