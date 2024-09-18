package com.mammon.member.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/2/21 16:36
 */
@Data
public class MemberRechargeOrderDto {

    /**
     * 会员id
     */
    private String memberId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 储值规则id
     */
    private String ruleId;

    /**
     * 购买数量
     */
    private long quantity;

    /**
     * 储值金额
     */
    private long rechargeAmount;

    /**
     * 赠送金额
     */
    private long giveAmount;

    /**
     * 付款金额
     */
    private long receivesAmount;

    /**
     * 赠送积分
     */
    private long giveIntegral;

    /**
     * 充值日期
     */
    private LocalDateTime rechargeTime;
}
