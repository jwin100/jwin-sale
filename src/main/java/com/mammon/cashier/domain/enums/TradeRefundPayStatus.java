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
public enum TradeRefundPayStatus implements IEnum<TradeRefundPayStatus> {
    SUBMIT(1, "退款提交成功"),
    SUCCESS(2, "退款完成"),
    FAILED(3, "退款失败"),
    ;

    private final int code;
    private final String name;
}
