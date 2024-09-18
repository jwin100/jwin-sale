package com.mammon.member.domain.dto;

import lombok.Data;

@Data
public class PayTypeDto {

    /**
     * 支付方式
     */
    private int payTypeId;

    /**
     * 应收
     */
    private long payAmount;

    private String authCode;
}
