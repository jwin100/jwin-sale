package com.mammon.print.channel.factory;

import com.mammon.config.ApplicationBean;
import com.mammon.print.channel.elind.ELindService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dcl
 * @since 2024/2/27 14:29
 */
public class PrintChannelFactory {

    public static final Map<String, BasePrintChannel> map = new HashMap<>();

    static {
        map.put("elind", ApplicationBean.getBean(ELindService.class));
    }

    public static BasePrintChannel get(String channel) {
        return map.get(channel);
    }
}
