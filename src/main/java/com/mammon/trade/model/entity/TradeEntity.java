package com.mammon.trade.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 支付流水
 *
 * @author dcl
 * @since 2024/3/1 16:13
 */
@Data
public class TradeEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    /**
     * 流水编号
     */
    private String tradeNo;

    /**
     * 支付渠道
     */
    private String channelId;

    /**
     * 支付模式
     */
    private int payMode;

    /**
     * 支付方式
     */
    private int payWay;

    private String orderNo;

    /**
     * 订单描述
     */
    private String orderSubject;

    /**
     * 订单金额(分)
     */
    private long orderAmount;

    /**
     * 商户下级(支付渠道生成)流水号
     */
    private String channelTradeNo;

    /**
     * 支付渠道生成订单号请求下级(银行)
     */
    private String bankOrderNo;

    /**
     * 支付渠道下级(银行生成)流水号
     */
    private String bankTradeNo;

    /**
     * 会员id
     */
    private String memberId;

    private String authCode;

    /**
     * 支付成功时间
     */
    private LocalDateTime successTime;

    /**
     * 买家实际付款金额
     */
    private long buyerPayAmount;

    /**
     * 订单状态（1，创建，2：待付款，3：付款完成，4：付款失败, 5：已撤销，6：已关闭，7：非法订单）
     */
    private int status;

    /**
     * 状态描述
     */
    private String describe;

    /**
     * 是否有退款（0：无退款，1：有退款）
     */
    private int refund;

    /**
     * 退款金额（成功退款后才计算）
     */
    private long refundAmount;

    private LocalDateTime createTime;
}
