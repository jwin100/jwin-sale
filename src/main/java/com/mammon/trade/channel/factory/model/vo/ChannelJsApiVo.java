package com.mammon.trade.channel.factory.model.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/4 17:47
 */
@Data
public class ChannelJsApiVo {

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

    /**
     * 预下单号
     */
    private String prepayId;

    /**
     * 预下单内容
     */
    private String prepayData;

    /**
     * 平台交易流水号
     */
    private String channelTradeNo;

    private String bankOrderNo;

    private String bankTradeNo;
}
