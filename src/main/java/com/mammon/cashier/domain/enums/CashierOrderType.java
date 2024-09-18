package com.mammon.cashier.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @date 2023-06-01 15:50:50
 */
@Getter
@AllArgsConstructor
public enum CashierOrderType implements IEnum<CashierOrderType> {
    AMOUNT(1, "计价"),
    COUNTED(2, "计次"),
    ;

    private final int code;
    private final String name;
}
