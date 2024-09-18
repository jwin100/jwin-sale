package com.mammon.trade.channel.hmpay;

import com.mammon.trade.channel.factory.ChannelPay;
import com.mammon.trade.channel.factory.model.dto.ChannelDto;
import com.mammon.trade.channel.factory.model.dto.ChannelRefundDto;
import com.mammon.trade.channel.factory.model.vo.*;
import com.mammon.trade.model.enums.TradeAuthStatus;
import com.mammon.trade.model.enums.TradeCommonStatus;
import com.mammon.trade.model.enums.TradeRefundStatus;
import org.springframework.stereotype.Service;

/**
 * @author dcl
 * @since 2024/3/1 18:18
 */
@Service
public class HmPayService implements ChannelPay {

    @Override
    public ChannelNativeVo channelNative(String channelCode, String channelConfig, ChannelDto dto) {
        ChannelNativeVo vo = new ChannelNativeVo();
        vo.setStatus(TradeCommonStatus.FAILED.getCode());
        vo.setDescribe("交易渠道未开通");
        return vo;
    }

    @Override
    public ChannelAuthVo channelAuth(String channelCode, String channelConfig, ChannelDto dto) {
        ChannelAuthVo vo = new ChannelAuthVo();
        vo.setStatus(TradeAuthStatus.FAILED.getCode());
        vo.setDescribe("交易渠道未开通");
        return vo;
    }

    @Override
    public ChannelJsApiVo channelJsApi(String channelCode, String channelConfig, ChannelDto dto) {
        ChannelJsApiVo vo = new ChannelJsApiVo();
        vo.setStatus(TradeCommonStatus.FAILED.getCode());
        vo.setDescribe("交易渠道未开通");
        return vo;
    }

    @Override
    public ChannelQueryVo channelQuery(String channelConfig, String tradeNo) {
        return null;
    }

    @Override
    public ChannelCloseVo channelClose(String channelConfig, String tradeNo) {
        ChannelCloseVo vo = new ChannelCloseVo();
        vo.setStatus(TradeCommonStatus.FAILED.getCode());
        vo.setDescribe("交易渠道未开通");
        return vo;
    }

    @Override
    public ChannelCancelVo channelCancel(String channelConfig, String tradeNo) {
        ChannelCancelVo vo = new ChannelCancelVo();
        vo.setStatus(TradeCommonStatus.FAILED.getCode());
        vo.setDescribe("交易渠道未开通");
        return vo;
    }

    @Override
    public ChannelRefundVo channelRefund(String channelConfig, ChannelRefundDto dto) {
        ChannelRefundVo vo = new ChannelRefundVo();
        vo.setStatus(TradeRefundStatus.FAILED.getCode());
        vo.setDescribe("交易渠道未开通");
        return vo;
    }

    @Override
    public ChannelRefundQueryVo channelRefundQuery(String channelConfig, String refundTradeNo) {
        return null;
    }
}
