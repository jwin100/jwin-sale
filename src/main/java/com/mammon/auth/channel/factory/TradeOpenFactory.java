package com.mammon.auth.channel.factory;

import com.mammon.auth.channel.wechat.TradeWechatOpenService;
import com.mammon.config.ApplicationBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dcl
 * @since 2024/8/5 23:22
 */
public class TradeOpenFactory {

    public static final Map<String, TradeOpenChannel> channels = new HashMap<>();

    static {
        channels.put(TradeChannelCode.WECHAT.getCode(), ApplicationBean.getBean(TradeWechatOpenService.class));
    }
}
