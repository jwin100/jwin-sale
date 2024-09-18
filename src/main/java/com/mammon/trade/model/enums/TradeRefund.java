package com.mammon.trade.model.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/3/1 18:07
 */
@Getter
@AllArgsConstructor
public enum TradeRefund implements IEnum<TradeRefund> {
    NOT_REFUND(0, "无退款"),
    HAVE_REFUND(1, "有退款"),
    ;

    private final int code;
    private final String name;
}
