package com.mammon.trade.channel.factory.model.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/7 16:55
 */
@Data
public class ChannelRefundVo {

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
     * 支付渠道流水号
     */
    private String channelTradeNo;

    /**
     * 银行订单号
     */
    private String bankOrderNo;

    /**
     * 银行流水号
     */
    private String bankTradeNo;

    /**
     * 交易方式
     */
    private int payWay;
}
