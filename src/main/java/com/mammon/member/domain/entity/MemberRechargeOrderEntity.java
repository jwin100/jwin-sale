package com.mammon.member.domain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会员储值订单
 *
 * @author dcl
 * @since 2024/2/21 14:53
 */
@Data
public class MemberRechargeOrderEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

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
     * 储值状态(0:未退款，1:已退款)
     */
    private int refunded;

    /**
     * 充值日期
     */
    private LocalDateTime rechargeTime;

    /**
     * 退款日期
     */
    private LocalDateTime refundTime;
}
