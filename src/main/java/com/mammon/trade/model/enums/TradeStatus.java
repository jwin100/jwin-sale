package com.mammon.trade.model.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/3/1 18:11
 */
@Getter
@AllArgsConstructor
public enum TradeStatus implements IEnum<TradeStatus> {
    CREATED(1, "订单已创建"),
    WAIT_PAYMENT(2, "等待支付"),
    SUCCESS(3, "交易成功"),
    FAILED(4, "交易失败"),
    CANCEL(5, "已撤销"),
    CLOSE(6, "已关闭"),
    ILLEGAL(7, "非法订单"),
    ;

    private final int code;
    private final String name;
}
