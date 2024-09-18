package com.mammon.trade.model.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/3/4 11:38
 */
@Getter
@AllArgsConstructor
public enum TradePayWay implements IEnum<TradePayWay> {
    WECHAT_PAY(1, "微信"),
    ALI_PAY(2, "支付宝"),
    UNION_PAY(3, "银联"),
    OTHER(99, "其他"),
    ;

    private final int code;
    private final String name;
}
