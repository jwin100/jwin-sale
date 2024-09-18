package com.mammon.clerk.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/4/7 17:03
 */
@Getter
@AllArgsConstructor
public enum CommissionRuleType implements IEnum<CommissionRuleType> {
    CASHIER(1, "销售提成"),
    RECHARGE(2, "储值提成"),
    COUNTED(3, "计次提成"),
    SERVICE(4, "服务提成"),
    ;

    private final int code;
    private final String name;
}
