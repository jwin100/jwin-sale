package com.mammon.cashier.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2023/11/16 15:03
 */
@Getter
@AllArgsConstructor
public enum CashierOrderCategory implements IEnum<CashierOrderCategory> {
    GOODS(1, "商品销售"),
    RECHARGE(2, "余额储值"),
    COUNTED(3, "计次开卡"),
    SERVICE(4, "服务"),
    ;

    private final int code;
    private final String name;
}
