package com.mammon.cashier.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CashierRefundMark implements IEnum<CashierRefundMark> {
    NONE(0, "无退货"),
    PART(1, "部分退货"),
    ALL(3, "全部退货"),
    ;

    private final int code;
    private final String name;
}
