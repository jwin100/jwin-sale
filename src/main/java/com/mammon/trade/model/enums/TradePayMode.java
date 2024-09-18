package com.mammon.trade.model.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/3/1 16:19
 */
@Getter
@AllArgsConstructor
public enum TradePayMode implements IEnum<TradePayMode> {
    NATIVE(1, "商户被扫"),
    AUTH(2, "商户主扫"),
    JSAPI(3, "JSAPI"),
    H5(3, "h5"),
    APP(3, "APP"),
    ;

    private final int code;
    private final String name;
}
