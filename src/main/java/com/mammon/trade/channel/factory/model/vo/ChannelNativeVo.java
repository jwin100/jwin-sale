package com.mammon.trade.channel.factory.model.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/4 17:05
 */
@Data
public class ChannelNativeVo {

    /**
     * 预下单状态
     * <p>
     * TradeNativeStatus
     */
    private int status;

    /**
     * 状态描述
     */
    private String describe;

    private String tradeNo;

    private String codeUrl;

    /**
     * 平台交易流水号
     */
    private String channelTradeNo;

    private String bankOrderNo;

    private String bankTradeNo;
}
