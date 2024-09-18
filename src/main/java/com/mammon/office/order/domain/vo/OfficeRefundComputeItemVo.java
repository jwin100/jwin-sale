package com.mammon.office.order.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-03-14 09:59:20
 */
@Data
public class OfficeRefundComputeItemVo {

    private String id;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 套餐包id
     */
    private String spuId;

    private String skuId;

    private String skuName;

    /**
     * 付款金额
     */
    private BigDecimal payableAmount;

    /**
     * 单位
     */
    private int unit;

    private String unitName;

    /**
     * 业务类型，版本，短信，连锁额度
     */
    private int type;

    private String typeName;

    /**
     * 状态(1：未生效:2：已生效，3：生效失败)
     */
    private int status;

    private String statusName;

    /**
     * 生效描述
     */
    private String activeMessage;

    private String remark;

    /**
     * 生效日期
     */
    private LocalDateTime activeTime;

    /**
     * 退款数量
     */
    private long refundQuantity;

    /**
     * 扣除金额
     */
    private BigDecimal deductAmount;

    /**
     * 扣除描述
     */
    private String deductMessage;

    /**
     * 实退金额
     */
    private BigDecimal refundAmount;
}
