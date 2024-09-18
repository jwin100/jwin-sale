package com.mammon.office.order.channel.factory;

import com.mammon.config.ApplicationBean;
import com.mammon.office.order.channel.alipay.AlipayChannel;
import com.mammon.office.order.channel.wechatpay.WechatpayChannel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dcl
 * @date 2023-03-02 14:52:15
 */
public class TradePayChannelFactory {

    private static final Map<String, BaseTradePayChannel> map = new HashMap<>();

    static {
        map.put("alipay", ApplicationBean.getBean(AlipayChannel.class));
        map.put("wechatpay", ApplicationBean.getBean(WechatpayChannel.class));
    }

    public static BaseTradePayChannel get(String channel) {
        return map.get(channel);
    }
}
