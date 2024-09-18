package com.mammon.cashier.channel.factory.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/8 15:03
 */
@Data
public class TradeMemberRefundDto {

    private long merchantNo;

    private long storeNo;

    private String accountId;

    private String refundNo;

    private String orderNo;

    private String memberId;

    private String countedId;

    private long countedTotal;

    /**
     * 退款金额
     */
    private long refundAmount;
}
