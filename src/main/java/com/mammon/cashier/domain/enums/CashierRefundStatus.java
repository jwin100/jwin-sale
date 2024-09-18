package com.mammon.cashier.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CashierRefundStatus implements IEnum<CashierRefundStatus> {
    REFUND_ING(1, "退款中"),
    REFUND_SUBMIT(2, "已提交"),
    REFUND_FINISH(3, "退款完成"),
    REFUND_CANCEL(4, "已取消"),
    ;

    private final int code;
    private final String name;
}
