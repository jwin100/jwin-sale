package com.mammon.cashier.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单交易信息
 */
@Data
public class CashierOrderPayEntity {

    private String id;

    private String orderId;

    /**
     * 支付方式
     */
    private int payCode;

    /**
     * 应收
     */
    private long payableAmount;

    /**
     * 实收
     */
    private long realityAmount;

    private String authCode;

    /**
     * 核销计次卡id
     */
    private String countedId;

    /**
     * 核销计次卡次数
     */
    private long countedTotal;

    /**
     * 付款流水号
     */
    private String tradeNo;

    /**
     * 付款日期
     */
    private LocalDateTime tradeTime;

    /**
     * 交易状态
     */
    private int status;

    private String message;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
