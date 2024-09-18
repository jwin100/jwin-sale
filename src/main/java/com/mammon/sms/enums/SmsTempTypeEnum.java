package com.mammon.sms.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信模板类型
 *
 * @author dcl
 * @since 2024/5/9 10:29
 */
@Getter
@AllArgsConstructor
public enum SmsTempTypeEnum implements IEnum<SmsTempTypeEnum> {
    LOGIN(101, SmsTypeEnum.CAPTCHA.getCode(), "登录验证码"),
    EDIT_PASSWORD(102, SmsTypeEnum.CAPTCHA.getCode(), "修改密码"),
    EDIT_PHONE(101, SmsTypeEnum.CAPTCHA.getCode(), "修改号码"),

    ORDER_CASHIER(201, SmsTypeEnum.NOTICE.getCode(), "销售通知"),
    ORDER_REFUND(202, SmsTypeEnum.NOTICE.getCode(), "退货通知"),
    MEMBER_REGISTER(203, SmsTypeEnum.NOTICE.getCode(), "会员注册"),
    MEMBER_RECHARGE(204, SmsTypeEnum.NOTICE.getCode(), "会员储值"),
    MEMBER_RECHARGE_CHANGE(205, SmsTypeEnum.NOTICE.getCode(), "储值余额变动"),
    MEMBER_COUNTED(206, SmsTypeEnum.NOTICE.getCode(), "计次开卡"),
    MEMBER_COUNTED_CHANGE(207, SmsTypeEnum.NOTICE.getCode(), "计次余额变动"),
    MEMBER_MAKE(208, SmsTypeEnum.NOTICE.getCode(), "会员预约"),
    BILL_NOTICE(209, SmsTypeEnum.NOTICE.getCode(), "账单通知"),
    MEMBER_BIRTHDAY(210, SmsTypeEnum.NOTICE.getCode(), "生日祝福"),

    FESTIVAL_BLESS(301, SmsTypeEnum.MARKET.getCode(), "节日祝福"),
    GOODS(302, SmsTypeEnum.MARKET.getCode(), "商品提醒"),
    MEMBER_MARKET(303, SmsTypeEnum.MARKET.getCode(), "会员营销"),
    FESTIVAL_SALE(304, SmsTypeEnum.MARKET.getCode(), "节日促销"),
    OTHER(399, SmsTypeEnum.MARKET.getCode(), "其他短信"),
    ;

    private final int code;
    private final int classify;
    private final String name;
}
