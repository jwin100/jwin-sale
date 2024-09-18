package com.mammon.market.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class MarketTimeCardRuleDto {

    private String name;

    /**
     * 卡类型
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
    private BigDecimal realAmount;

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

    private List<String> spuIds = new ArrayList<>();

    private int sort;

    private String remark;
}
