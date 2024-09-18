package com.mammon.cashier.channel.factory.vo;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-04-04 15:25:45
 */
@Data
public class TradeMemberPayVo {
    /**
     * 支付结果
     */
    private int status;

    /**
     * 结果描述
     */
    private String describe;

    /**
     * 第三方流水号
     */
    private String tradeNo;

    /**
     * 预支付返回信息
     */
    private String credential;
}
