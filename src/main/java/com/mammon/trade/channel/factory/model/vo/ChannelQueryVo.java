package com.mammon.trade.channel.factory.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/3/4 18:24
 */
@Data
public class ChannelQueryVo {

    private String tradeNo;

    /**
     * TradeStatus
     * <p>
     * 交易状态
     */
    private int status;

    /**
     * 状态描述
     */
    private String describe;

    /**
     * 买家实际付款金额
     */
    private long buyerPayAmount;

    /**
     * 是否有退款
     */
    private boolean refund;

    /**
     * 退款金额
     */
    private long refundAmount;

    /**
     * 交易成功时间
     */
    private LocalDateTime successTime;
}
