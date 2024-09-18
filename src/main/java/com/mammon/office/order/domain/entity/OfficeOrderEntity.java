package com.mammon.office.order.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-02-02 13:05:10
 * 订单
 */
@Data
public class OfficeOrderEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    /**
     * 操作人
     */
    private String accountId;

    private String orderNo;

    private String subject;

    /**
     * 应收
     */
    private long payableAmount;

    /**
     * 支付配置信息
     */
    private String configId;

    /**
     * 支付方式
     */
    private int payType;

    /**
     * 订单状态(1:待支付，2：支付成功，3：支付失败，4：有退款，8：已关闭)
     */
    private int status;

    private String payMessage;

    /**
     * 订单来源
     */
    private int source;

    private String remark;

    /**
     * 支付日期
     */
    private LocalDateTime payTime;

    /**
     * 支付通道流水号
     */
    private String tradeNo;

    /**
     * 订单创建日期
     */
    private LocalDateTime createTime;

    /**
     * 修改日期
     */
    private LocalDateTime updateTime;
}
