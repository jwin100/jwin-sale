package com.mammon.trade.channel.factory.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-03-14 00:16:39
 */
@Data
public class ChannelRefundNotifyVo {
    /**
     * TradeRefundStatus
     */
    private int status;

    /**
     * 交易描述
     */
    private String describe;

    private String refundTradeNo;

    private int payWay;

    /**
     * 交易金额
     */
    private long orderAmount;

    private LocalDateTime refundSuccessTime;
}
