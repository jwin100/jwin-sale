package com.mammon.trade.model.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/7 17:32
 */
@Data
public class TradeRefundVo {

    private int status;

    private String describe;

    private String orderTradeNo;

    private String refundTradeNo;
}
