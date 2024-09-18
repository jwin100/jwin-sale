package com.mammon.trade.channel.factory;

import com.mammon.trade.channel.factory.model.vo.ChannelNotifyVo;
import com.mammon.trade.channel.factory.model.vo.ChannelRefundNotifyVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author dcl
 * @since 2024/3/6 11:31
 */
public interface ChannelPayNotify {

    ChannelNotifyVo tradeNotify(Map<String, String> paramMap);

    ChannelRefundNotifyVo tradeRefundNotify(Map<String, String> paramMap);

    default Object responseSuccess() {
        return "success";
    }

    default Object responseFail(String message) {
        return "fail";
    }
}
