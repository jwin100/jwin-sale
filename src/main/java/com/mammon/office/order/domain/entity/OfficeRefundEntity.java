package com.mammon.office.order.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-03-02 15:21:36
 * 支持部分退款
 */
@Data
public class OfficeRefundEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String accountId;

    private String orderId;

    private String refundNo;

    /**
     * 退款金额
     */
    private long refundAmount;

    /**
     * 退款方式
     */
    private int refundType;

    private int status;

    private String refundMessage;

    /**
     * 退款日期
     */
    private LocalDateTime refundTime;

    /**
     * 支付通道流水号
     */
    private String tradeNo;

    private String configId;

    private String remark;

    /**
     * 订单创建日期
     */
    private LocalDateTime createTime;

    /**
     * 修改日期
     */
    private LocalDateTime updateTime;

}
