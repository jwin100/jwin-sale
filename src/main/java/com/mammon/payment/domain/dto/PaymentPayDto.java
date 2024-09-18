package com.mammon.payment.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-05-17 10:46:38
 */
@Data
public class PaymentPayDto {

    private Integer code;

    private Long amount;

    private Long total;

    private String authCode;

    private String payId;
}
