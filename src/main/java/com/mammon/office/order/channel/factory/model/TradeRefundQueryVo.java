package com.mammon.office.order.channel.factory.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-02-02 17:16:44
 */
@Data
public class TradeRefundQueryVo {

    private String refundNo;

    /**
     * 支付通道交易号
     */
    private String tradeNo;

    /**
     * 退款状态
     */
    private int status;

    private String message;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    private LocalDateTime refundTime;
}
