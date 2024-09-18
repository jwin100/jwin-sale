package com.mammon.trade.model.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/6 10:38
 */
@Data
public class TradeCancelVo {

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
}
