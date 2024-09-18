package com.mammon.office.order.domain.enums;

import com.mammon.enums.IEnum;
import lombok.Getter;

/**
 * @author dcl
 * @date 2023-03-02 15:33:05
 */
@Getter
public enum OfficeOrderPayType implements IEnum<OfficeOrderPayType> {
    aliPay(1, "支付宝", "alipay", OfficeOrderPayTypeConst.alipayPaymentIconUrl, false),
    wechatPay(2, "微信", "wechatpay", OfficeOrderPayTypeConst.wechatPaymentIconUrl, true),
    ;

    private final int code;

    private final String name;

    private final String channelCode;

    private final String icon;

    private final boolean status;

    private OfficeOrderPayType(int code, String name, String channelCode, String icon, boolean status) {
        this.code = code;
        this.name = name;
        this.channelCode = channelCode;
        this.icon = icon;
        this.status = status;
    }
}
