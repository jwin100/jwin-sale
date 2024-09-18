package com.mammon.cashier.channel.factory.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/3/7 14:47
 */
@Getter
@AllArgsConstructor
public enum TradeMemberStatus implements IEnum<TradeMemberStatus> {
    SUCCESS(1, "交易成功"),
    FAILED(2, "交易失败"),
    ;

    private final int code;
    private final String name;
}
