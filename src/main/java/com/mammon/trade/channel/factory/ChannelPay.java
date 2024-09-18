package com.mammon.trade.channel.factory;

import com.mammon.trade.channel.factory.model.dto.ChannelDto;
import com.mammon.trade.channel.factory.model.dto.ChannelRefundDto;
import com.mammon.trade.channel.factory.model.vo.*;

/**
 * @author dcl
 * @since 2024/3/1 18:21
 */
public interface ChannelPay {

    default String getNotifyUrl(String channelCode) {
        return String.format("https://api.jwin100.cn/merchant/open/office-order/notify/pay/%s", channelCode);
    }

    // C扫B PC
    // B扫C PC
    // JSAPI 微信公众号，小程序
    // H5 移动端网页
    // APP 移动端app

    ChannelNativeVo channelNative(String channelCode, String channelConfig, ChannelDto dto);

    ChannelAuthVo channelAuth(String channelCode, String channelConfig, ChannelDto dto);

    ChannelJsApiVo channelJsApi(String channelCode, String channelConfig, ChannelDto dto);

    ChannelQueryVo channelQuery(String channelConfig, String tradeNo);

    ChannelCloseVo channelClose(String channelConfig, String tradeNo);

    ChannelCancelVo channelCancel(String channelConfig, String tradeNo);

    ChannelRefundVo channelRefund(String channelConfig, ChannelRefundDto dto);

    ChannelRefundQueryVo channelRefundQuery(String channelConfig, String refundTradeNo);
}
