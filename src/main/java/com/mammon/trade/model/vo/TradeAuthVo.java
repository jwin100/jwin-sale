package com.mammon.trade.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/3/5 10:13
 */
@Data
public class TradeAuthVo {

    /**
     * 支付状态
     */
    private int status;

    /**
     * 状态描述
     */
    private String describe;

    private String orderNo;

    /**
     * 交易流水号
     */
    private String tradeNo;

    /**
     * 支付模式
     */
    private int payMode;

    /**
     * 支付方式
     */
    private int payWay;

    /**
     * 交易成功时间
     */
    private LocalDateTime successTime;
}
