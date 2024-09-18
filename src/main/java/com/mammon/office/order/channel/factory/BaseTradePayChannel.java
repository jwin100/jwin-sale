package com.mammon.office.order.channel.factory;

import com.mammon.office.order.channel.factory.model.TradeNativeVo;
import com.mammon.office.order.channel.factory.model.TradePayVo;
import com.mammon.office.order.channel.factory.model.TradeRefundQueryVo;
import com.mammon.office.order.channel.factory.model.TradeRefundVo;
import com.mammon.office.order.domain.entity.OfficeOrderEntity;

import java.math.BigDecimal;

/**
 * @author dcl
 * @date 2023-02-02 15:42:18
 */
public interface BaseTradePayChannel {

    default String getNotifyUrl(String channelCode) {
        return String.format("https://api.jwin100.cn/merchant/open/office-order/notify/pay/%s", channelCode);
    }

    /**
     * 创建预支付
     *
     * @param configStr
     * @param order
     */
    String tradeNative(String channelCode, String configStr, OfficeOrderEntity order);

    /**
     * 订单查询
     *
     * @param configStr
     * @param orderNo
     * @return
     */
    TradePayVo tradeQuery(String configStr, String orderNo);

    TradeRefundVo tradeRefund(String configStr, String orderNo, String refundNo, BigDecimal refundAmount);

    TradeRefundQueryVo tradeRefundQuery(String configStr, String orderNo, String refundNo);
}
