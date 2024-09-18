package com.mammon.office.order.channel.factory.model;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-02-02 16:54:42
 */
@Data
public class TradeNativeVo {

    /**
     * 1:成功，2:失败
     */
    private int code;

    /**
     * 交易描述
     */
    private String message;

    /**
     * 二维码url
     */
    private String qrCode;

    private String orderNo;
}
