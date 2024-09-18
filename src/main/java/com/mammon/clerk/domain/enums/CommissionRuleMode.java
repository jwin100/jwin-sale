package com.mammon.clerk.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/4/8 13:36
 */
@Getter
@AllArgsConstructor
public enum CommissionRuleMode implements IEnum<CommissionRuleMode> {
    CASHIER_PIECE(1, "计件", CommissionRuleType.CASHIER, CommissionRuleUnit.FIXED),
    CASHIER_AMOUNT(2, "按销售额比例", CommissionRuleType.CASHIER, CommissionRuleUnit.PERCENTAGE),
    CASHIER_PROFIT(3, "按利润比例", CommissionRuleType.CASHIER, CommissionRuleUnit.PERCENTAGE),
    RECHARGE_AMOUNT(4, "按储值收款金额比例计算", CommissionRuleType.RECHARGE, CommissionRuleUnit.PERCENTAGE),
    COUNTED_AMOUNT(5, "按销售额比例计算", CommissionRuleType.COUNTED, CommissionRuleUnit.PERCENTAGE),
    SERVICE_NUMBER(6, "按次", CommissionRuleType.SERVICE, CommissionRuleUnit.FIXED),
    SERVICE_DURATION(7, "计时", CommissionRuleType.SERVICE, CommissionRuleUnit.FIXED),
    ;

    private final int code;
    private final String name;
    private final CommissionRuleType type;
    private final CommissionRuleUnit unit;
}
