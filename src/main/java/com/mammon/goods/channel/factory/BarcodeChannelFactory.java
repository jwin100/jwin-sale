package com.mammon.goods.channel.factory;

import com.mammon.config.ApplicationBean;
import com.mammon.goods.channel.wanwei.WanWeiBarcodeService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dcl
 * @since 2024/4/11 18:22
 */
public class BarcodeChannelFactory {

    public static Map<String, BarcodeChannel> factory;

    static {
        factory = new HashMap<>();
        factory.put("wanwei", ApplicationBean.getBean(WanWeiBarcodeService.class));
    }

    public static BarcodeChannel get(String channelCode) {
        return factory.get(channelCode);
    }
}
