package com.mammon.clerk.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/4/8 13:45
 */
@Getter
@AllArgsConstructor
public enum CommissionRuleUnit implements IEnum<CommissionRuleUnit> {
    FIXED(1, "元"),
    PERCENTAGE(2, "%"),
    ;

    private final int code;
    private final String name;
}
