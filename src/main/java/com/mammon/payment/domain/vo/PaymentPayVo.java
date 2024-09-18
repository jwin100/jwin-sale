package com.mammon.payment.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author dcl
 * @date 2023-05-17 10:51:29
 */
@Data
public class PaymentPayVo {

    /**
     * 1：成功，2：失败
     */
    private int status;

    private String message;
}
