package com.mammon.trade.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/3/7 16:20
 */
@Data
public class TradeRefundEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String tradeNo;

    private String refundTradeNo;

    private String refundSubject;

    private long refundAmount;

    /**
     * 支付渠道
     */
    private String channelId;

    /**
     * 支付渠道流水号
     */
    private String channelTradeNo;

    private String bankOrderNo;

    private String bankTradeNo;

    private int payWay;

    private int status;

    /**
     * 状态描述
     */
    private String describe;

    private LocalDateTime refundSuccessTime;

    private LocalDateTime createTime;
}
