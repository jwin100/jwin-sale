package com.mammon.office.order.domain.enums;

import com.mammon.enums.IEnum;
import lombok.Getter;

/**
 * @author dcl
 * @date 2023-03-05 20:45:19
 */
@Getter
public enum OfficeOrderStatus implements IEnum<OfficeOrderStatus> {
    /**
     * 订单创建初始状态
     */
    waitPay(1, "待支付"),
    /**
     * 支付成功后
     */
    paySuccess(2, "支付成功"),
    /**
     * 支超时未支付
     */
    payCancel(3, "支付失败"),
    /**
     * 订单全部生效后转为已完成
     */
    payFinish(4, "已完成"),
    refunding(5, "退款中"),
    refunded(6, "已退款")
    ;

    private int code;

    private String name;

    private OfficeOrderStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
