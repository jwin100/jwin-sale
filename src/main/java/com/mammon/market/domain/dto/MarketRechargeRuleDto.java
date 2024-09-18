package com.mammon.market.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MarketRechargeRuleDto {
    /**
     * 实付金额
     */
    private BigDecimal realAmount;

    /**
     * 赠送金额
     */
    private BigDecimal giveAmount;

    /**
     * 送积分
     */
    private long giveIntegral;

    private String remark;

    private int sort;
}
