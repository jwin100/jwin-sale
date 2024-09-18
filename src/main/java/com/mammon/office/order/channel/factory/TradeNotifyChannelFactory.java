package com.mammon.office.order.channel.factory;

import com.mammon.config.ApplicationBean;
import com.mammon.office.order.channel.alipay.AlipayNotifyChannel;
import com.mammon.office.order.channel.wechatpay.WechatpayChannel;
import com.mammon.office.order.channel.wechatpay.WechatpayNotifyChannel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dcl
 * @date 2023-03-13 22:35:59
 */
public class TradeNotifyChannelFactory {

    public static final Map<String, BaseTradeNotifyChannel> map = new HashMap<>();

    static {
        map.put("alipay", ApplicationBean.getBean(AlipayNotifyChannel.class));
        map.put("wechatpay", ApplicationBean.getBean(WechatpayNotifyChannel.class));
    }

    public static BaseTradeNotifyChannel get(String channel) {
        return map.get(channel);
    }
}
