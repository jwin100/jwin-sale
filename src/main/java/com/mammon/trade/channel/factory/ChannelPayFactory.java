package com.mammon.trade.channel.factory;

import com.mammon.config.ApplicationBean;
import com.mammon.exception.CustomException;
import com.mammon.trade.channel.hmpay.HmPayNotifyService;
import com.mammon.trade.channel.hmpay.HmPayService;
import com.mammon.trade.consts.TradeChannelCode;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dcl
 * @since 2024/3/1 18:18
 */
@Slf4j
public class ChannelPayFactory {

    public static final Map<String, ChannelPay> map = new HashMap<>();
    public static final Map<String, ChannelPayNotify> notifyMap = new HashMap<>();

    static {
        map.put(TradeChannelCode.hmPay, ApplicationBean.getBean(HmPayService.class));

        notifyMap.put(TradeChannelCode.hmPay, ApplicationBean.getBean(HmPayNotifyService.class));
    }

    public static ChannelPay get(String channelCode) {
        if (!map.containsKey(channelCode)) {
            log.info("交易渠道{}未实现", channelCode);
            throw new CustomException("当前交易渠道未实现");
        }
        return map.get(channelCode);
    }

    public static ChannelPayNotify getNotify(String channelCode) {
        if (!notifyMap.containsKey(channelCode)) {
            log.info("交易渠道{}未实现", channelCode);
            throw new CustomException("当前交易渠道未实现");
        }
        return notifyMap.get(channelCode);
    }
}
