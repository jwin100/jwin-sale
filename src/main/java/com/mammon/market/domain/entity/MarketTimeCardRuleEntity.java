package com.mammon.market.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 计次卡规则
 */
@Data
public class MarketTimeCardRuleEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String name;

    /**
     * 有效期(0:永久有效,1:开卡时+指定月)
     */
    private int expireType;

    private long expireMonth;

    /**
     * 计次次数
     */
    private int timeTotal;

    /**
     * 售价
     */
    private long realAmount;

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

    private String spuIds;

    private int status;

    private int sort;

    private String remark;

    private String accountId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private int deleted;
}
