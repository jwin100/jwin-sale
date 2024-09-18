package com.mammon.trade.model.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/3/5 11:32
 */
@Getter
@AllArgsConstructor
public enum TradeCommonStatus implements IEnum<TradeCommonStatus> {
    SUCCESS(1, "请求成功"),
    FAILED(2, "请求失败"),
    ;

    private final int code;
    private final String name;
}
