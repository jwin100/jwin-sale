package com.mammon.cashier.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/3/7 15:15
 */
@Getter
@AllArgsConstructor
public enum TradeOrderPayStatus implements IEnum<TradeOrderPayStatus> {
    SUCCESS(1, "交易成功"),
    FAILED(2, "交易失败"),
    WAITING(3, "等待付款"),
    ;

    private final int code;
    private final String name;
}
