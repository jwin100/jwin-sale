package com.mammon.cashier.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CashierOrderStatus implements IEnum<CashierOrderStatus> {
    /**
     * 订单创建成功，等待付款
     */
    WAIT(1, "待付款"),

    PARTIAL(2, "部分付款"),
    /**
     * 支付完成
     */
    FINISH(3, "已完成"),
    /**
     * 支付失败或超时未支付或全额退款后(订单关闭)
     */
    CLOSE(4, "已取消"),
    ;

    private final int code;
    private final String name;
}
