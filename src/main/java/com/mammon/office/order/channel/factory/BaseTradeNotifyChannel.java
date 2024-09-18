package com.mammon.office.order.channel.factory;

import com.mammon.office.order.channel.factory.model.PayNotifyVo;
import com.mammon.office.order.channel.factory.model.RefundNotifyVo;
import com.mammon.office.order.channel.factory.model.TradePayVo;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author dcl
 * @date 2023-03-13 22:35:22
 */
public interface BaseTradeNotifyChannel {

    TradePayVo payNotify(String channelCode, String configStr, HttpServletRequest request);

    RefundNotifyVo refundNotify(Map<String, String> paramMap);

    default Object responseSuccess() {
        return "success";
    }

    default Object responseFail() {
        return "fail";
    }

}
