package com.mammon.trade.channel.factory.model.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/7 16:55
 */
@Data
public class ChannelRefundDto {

    /**
     * 交易流水号
     */
    private String tradeNo;

    /**
     * 退款流水号
     */
    private String refundTradeNo;

    /**
     * 退款金额
     */
    private long refundAmount;
}
