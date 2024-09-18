package com.mammon.trade.model.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/3/7 17:25
 */
@Getter
@AllArgsConstructor
public enum TradeRefundStatus implements IEnum<TradeRefundStatus> {
    SUBMIT(1, "退款提交成功"),
    SUCCESS(2, "退款完成"),
    FAILED(3, "退款失败"),
    ;

    private final int code;
    private final String name;
}
