package com.mammon.trade.channel.factory.model.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/7 16:56
 */
@Data
public class ChannelRefundQueryVo {

    private String refundTradeNo;

    /**
     * 退款状态
     * <p>
     * TradeRefundStatus
     */
    private int status;

    /**
     * 状态描述
     */
    private String describe;

    /**
     * 退款金额
     */
    private long refundAmount;

    /**
     * 成功退款金额
     */
    private long refundSuccessAmount;

    private String channelTradeNo;

    private String bankOrderNo;

    private String bankTradeNo;
}
