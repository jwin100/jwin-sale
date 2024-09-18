package com.mammon.office.order.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-02-02 13:05:22
 */
@Data
public class OfficeOrderItemEntity {

    private String id;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 套餐包id
     */
    private String spuId;

    /**
     * 套餐包sku
     */
    private String skuId;

    /**
     * 数量
     */
    private long quantity;

    /**
     * 业务类型，短信、版本、连锁额度
     */
    private int type;

    /**
     * 单位
     */
    private int unit;

    /**
     * 状态(1：未生效:2：已生效，3：生效失败)
     */
    private int status;

    /**
     * 生效描述
     */
    private String activeMessage;

    /**
     * 付款金额
     */
    private long payableAmount;

    private String remark;

    /**
     * 生效日期
     */
    private LocalDateTime activeTime;

    /**
     * 绑定到门店
     */
    private long bindStoreNo;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;

    /**
     * 修改日期
     */
    private LocalDateTime updateTime;
}
