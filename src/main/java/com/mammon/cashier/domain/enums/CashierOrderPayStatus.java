package com.mammon.cashier.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @date 2023-04-06 00:52:43
 */
@Getter
@AllArgsConstructor
public enum CashierOrderPayStatus implements IEnum<CashierOrderPayStatus> {
    PAYING(1, "付款中"),
    PAY_SUCCESS(2, "付款完成"),
    PAY_ERROR(3, "付款失败"),
    ;

    private int code;
    private String name;
}
