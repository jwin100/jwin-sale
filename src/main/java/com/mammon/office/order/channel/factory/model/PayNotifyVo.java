package com.mammon.office.order.channel.factory.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-03-14 00:15:56
 */
@Data
public class PayNotifyVo {

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 支付通道交易号
     */
    private String tradeNo;

    /**
     * 交易状态(1:待付款，2:交易超时，3：交易成功，4:交易结束, 0:请求异常)
     */
    private int status;

    private String message;

    /**
     * 交易金额
     */
    private double amount;

    private LocalDateTime payTime;
}
