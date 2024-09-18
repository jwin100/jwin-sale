package com.mammon.cashier.channel.factory.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/7 14:31
 */
@Data
public class TradeMemberPayDto {

    private long merchantNo;

    private long storeNo;

    private String accountId;

    private String orderNo;

    private String memberId;

    private String subject;

    private long amount;

    private String countedId;

    private long countedTotal;
}
