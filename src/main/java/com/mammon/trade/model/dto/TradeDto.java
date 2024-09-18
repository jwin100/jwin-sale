package com.mammon.trade.model.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/4 14:59
 */
@Data
public class TradeDto {

    private long merchantNo;

    private long storeNo;

    /**
     * 订单号
     */
    private String orderNo;

    private String orderSubject;

    private long orderAmount;

    private String memberId;

    private String authCode;
}
