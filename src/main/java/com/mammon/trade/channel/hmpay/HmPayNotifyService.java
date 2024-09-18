package com.mammon.trade.channel.hmpay;

import com.mammon.trade.channel.factory.ChannelPayNotify;
import com.mammon.trade.channel.factory.model.vo.ChannelNotifyVo;
import com.mammon.trade.channel.factory.model.vo.ChannelRefundNotifyVo;
import com.mammon.trade.model.enums.TradeStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author dcl
 * @since 2024/3/6 14:42
 */
@Service
public class HmPayNotifyService implements ChannelPayNotify {

    @Override
    public ChannelNotifyVo tradeNotify(Map<String, String> paramMap) {
        return null;
    }

    @Override
    public ChannelRefundNotifyVo tradeRefundNotify(Map<String, String> paramMap) {
        return null;
    }

    @Override
    public Object responseSuccess() {
        return "SUCCESS";
    }

    @Override
    public Object responseFail(String message) {
        return "FAILED";
    }
}
