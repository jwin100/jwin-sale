package com.mammon.trade.model.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/5 13:18
 */
@Data
public class TradeJsApiVo {

    private int status;

    /**
     * 状态描述
     */
    private String describe;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 流水号
     */
    private String tradeNo;

    /**
     * 预下单号
     */
    private String prepayId;

    /**
     * 预下单内容
     */
    private String prepayData;
}
