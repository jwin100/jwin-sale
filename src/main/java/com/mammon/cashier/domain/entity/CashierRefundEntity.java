package com.mammon.cashier.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CashierRefundEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    /**
     * 退款编号
     */
    private String refundNo;

    /**
     * 退款交易流水号
     */
    private String tradeNo;

    /**
     * 订单id
     */
    private String orderId;

    private int type;

    private int category;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单交易流水号
     */
    private String orderTradeNo;

    private String subject;

    /**
     * 原价
     */
    private long originalAmount;

    /**
     * 手动调整金额
     */
    private long adjustAmount;

    /**
     * 应退
     */
    private long payableAmount;

    /**
     * 实退
     */
    private long realityAmount;

    /**
     * 退积分
     */
    private long integral;

    /**
     * 退计次
     */
    private long countedTotal;

    /**
     * 退款方式,0:原路退回，1:自行选择
     */
    private int refundMode;

    /**
     * 退款方式(JsonString)
     */
    private String payType;

    /**
     * 订单状态
     */
    private int status;

    /**
     * 订单状态描述
     */
    private String message;

    /**
     * 发送短信,1:发送,0:不发
     */
    private int sendSms;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 操作人(下单人)
     */
    private String operationId;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 订单创建日期
     */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
