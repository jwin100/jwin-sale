package com.mammon.trade.model.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/8 10:11
 */
@Data
public class TradeRefundDto {

    private String tradeNo;

    private long refundAmount;
}
