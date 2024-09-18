package com.mammon.trade.channel.factory.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-02-02 17:16:44
 */
@Data
public class ChannelNotifyVo {
    /**
     * TradeStatus
     */
    private int status;

    /**
     * 状态描述
     */
    private String describe;

    /**
     * 支付通道交易号
     */
    private String tradeNo;

    /**
     * 平台交易流水号
     */
    private String channelTradeNo;

    private String bankOrderNo;

    private String bankTradeNo;

    private int payMode;

    private int payWay;

    /**
     * 交易金额
     */
    private long orderAmount;

    /**
     * 买家支付金额
     */
    private long payerAmount;

    private LocalDateTime successTime;
}
