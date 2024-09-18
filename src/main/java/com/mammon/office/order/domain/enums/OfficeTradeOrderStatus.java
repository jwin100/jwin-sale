package com.mammon.office.order.domain.enums;

import com.mammon.enums.IEnum;
import lombok.Getter;

/**
 * @author dcl
 * @date 2023-03-20 17:59:47
 */
@Getter
public enum OfficeTradeOrderStatus implements IEnum<OfficeTradeOrderStatus> {
    waitPay(1, "待支付"),
    paySuccess(2, "支付成功"),
    payCancel(3, "支付失败"),
    refunding(4, "退款中"),
    refunded(5, "已退款"),
    refundCancel(6, "退款失败"),
    ;

    private int code;

    private String name;

    private OfficeTradeOrderStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
