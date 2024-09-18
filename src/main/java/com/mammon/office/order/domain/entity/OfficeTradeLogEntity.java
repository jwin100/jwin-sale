package com.mammon.office.order.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-03-02 13:22:22
 */
@Data
public class OfficeTradeLogEntity {

    private String id;

    /**
     * 订单id，退货单id
     */
    private String tradeNo;

    /**
     * 支付方式
     */
    private int payType;

    private String configId;

    private String appId;

    /**
     * 业务类型，预支付，支付，查单，退款，撤单，取消
     */
    private String tradeType;

    /**
     * 请求参数
     */
    private String payParams;

    /**
     * 返回结果
     */
    private String payResult;

    private String remark;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;

    /**
     * 修改日期
     */
    private LocalDateTime updateTime;
}
