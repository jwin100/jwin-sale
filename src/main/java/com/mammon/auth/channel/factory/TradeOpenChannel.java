package com.mammon.auth.channel.factory;

/**
 * @author dcl
 * @since 2024/8/5 23:22
 */
public interface TradeOpenChannel {

    /**
     * 获取用户openId
     *
     * @param code 获取openId凭证
     * @return
     */
    String getOpenId(String code);

    /**
     * 获取授权手机号
     *
     * @param code 获取手机号凭证
     * @return 手机号
     */
    String getPhoneNumber(String code);
}
