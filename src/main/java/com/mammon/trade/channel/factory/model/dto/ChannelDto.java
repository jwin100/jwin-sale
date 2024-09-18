package com.mammon.trade.channel.factory.model.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/5 15:44
 */
@Data
public class ChannelDto {

    /**
     * 支付中心流水号
     */
    private String tradeNo;

    private String orderSubject;

    private long orderAmount;

    private String authCode;
}
