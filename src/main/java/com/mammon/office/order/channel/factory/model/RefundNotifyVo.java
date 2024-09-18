package com.mammon.office.order.channel.factory.model;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-03-14 00:16:39
 */
@Data
public class RefundNotifyVo {
    /**
     * 1:成功，2:失败
     */
    private int code;

    /**
     * 交易描述
     */
    private String message;

    private String orderNo;

    private String refundNo;
}
