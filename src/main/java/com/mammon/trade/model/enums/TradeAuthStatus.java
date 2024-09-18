package com.mammon.trade.model.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/3/5 11:28
 */
@Getter
@AllArgsConstructor
public enum TradeAuthStatus implements IEnum<TradeAuthStatus> {
    SUCCESS(1, "交易成功"),
    FAILED(2, "交易失败"),
    WAITING(3, "订单支付"),
    ;

    private final int code;
    private final String name;
}
