package com.mammon.cashier.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @date 2023-04-06 10:10:04
 */
@Getter
@AllArgsConstructor
public enum CashierRefundPayStatus implements IEnum<CashierRefundPayStatus> {
    REFUND_ING(1, "退款提交成功"),
    REFUND_FINISH(2, "退款完成"),
    REFUND_ERROR(3, "退款异常"),
    ;

    private final int code;
    private final String name;
}
