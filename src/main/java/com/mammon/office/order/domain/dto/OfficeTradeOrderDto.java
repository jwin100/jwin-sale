package com.mammon.office.order.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-03-20 17:39:55
 */
@Data
public class OfficeTradeOrderDto {
    
    private String outTradeNo;

    private String tradeNo;

    /**
     * 支付渠道,1:alipay,2:wechat
     */
    private int payChannel;

    /**
     * 业务类型，1：支付，2：退款
     */
    private int type;

    private long totalAmount;

    private String appId;

    private String configId;

    private String configStr;

    private int status;

    private String message;

    private LocalDateTime tradeTime;
}
