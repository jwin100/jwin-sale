package com.mammon.cashier.channel.factory.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author dcl
 * @date 2023-04-06 11:19:30
 */
@Data
public class TradeMemberRefundVo {

    /**
     * TradeMemberStatus
     */
    private int status;

    /**
     * 退款描述
     */
    private String describe;

    private String refundTradeNo;
}
