package com.mammon.trade.channel.factory.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/3/4 17:36
 */
@Data
public class ChannelAuthVo {

    private String tradeNo;

    /**
     * 交易状态
     * <p>
     * TradeAuthStatus
     */
    private int status;

    /**
     * 状态描述
     */
    private String describe;

    /**
     * 平台交易流水号
     */
    private String channelTradeNo;

    private String bankOrderNo;

    private String bankTradeNo;

    /**
     * 交易成功时间
     */
    private LocalDateTime successTime;
}
