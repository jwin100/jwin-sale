package com.mammon.trade.consts;

import com.mammon.exception.CustomException;
import com.mammon.trade.model.enums.TradePayWay;
import org.apache.commons.lang3.StringUtils;

/**
 * @author dcl
 * @since 2024/3/4 11:36
 */
public class AuthCodeConst {

    private static final String BC_WECHAT_REGEX = "^1[0-5]\\d{16}";
    /**
     * 支付宝code匹配
     */
    private static final String BC_ALI_REGEX = "^(2[5-9]|30)\\d{14,22}";
    /**
     * 银联云闪付code匹配
     */
    private static final String BC_UNION_REGEX = "^62\\d{17}";

    /**
     * 授权码模式根据支付授权码识别支付方式
     *
     * @param authCode
     * @return
     */
    public static TradePayWay convertPayWay(String authCode) {
        if (StringUtils.isBlank(authCode)) {
            throw new CustomException("授权码为空");
        }
        if (authCode.matches(BC_WECHAT_REGEX)) {
            return TradePayWay.WECHAT_PAY;
        } else if (authCode.matches(BC_ALI_REGEX)) {
            return TradePayWay.ALI_PAY;
        } else if (authCode.matches(BC_UNION_REGEX)) {
            return TradePayWay.UNION_PAY;
        } else {
            return TradePayWay.OTHER;
        }
    }
}
