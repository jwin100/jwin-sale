package com.mammon.office.order.channel.factory.model;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-03-06 16:42:52
 */
@Data
public class TradeRefundVo {

    /**
     * 1:成功，2:失败
     */
    private int code;

    /**
     * 交易描述
     */
    private String message;

    private String orderNo;

    private String refundNo;

    private String tradeNo;
}
