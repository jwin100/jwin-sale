package com.mammon.market.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 储值规则
 */
@Data
public class MarketRechargeRuleEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    /**
     * 储值金额(不重复)
     */
    private long prepaidAmount;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 实付金额
     */
    private long realAmount;

    /**
     * 赠送金额
     */
    private long giveAmount;

    /**
     * 送积分
     */
    private long giveIntegral;

    /**
     * 送优惠券id
     */
    private String giveCouponId;

    /**
     * 送优惠券数量
     */
    private long giveCouponTotal;

    private int status;

    private int sort;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private int deleted;
}
