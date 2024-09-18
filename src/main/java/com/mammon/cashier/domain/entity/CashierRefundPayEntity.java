package com.mammon.cashier.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CashierRefundPayEntity {

    private String id;

    private String refundId;

    /**
     * 支付方式
     */
    private int payCode;

    /**
     * 应退
     */
    private long payableAmount;

    /**
     * 实退
     */
    private long realityAmount;

    private String countedId;

    private long countedTotal;

    /**
     * 退款流水号
     */
    private String tradeNo;

    /**
     * 退款日期
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
