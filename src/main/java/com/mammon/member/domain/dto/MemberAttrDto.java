package com.mammon.member.domain.dto;

import lombok.Data;

@Data
public class MemberAttrDto {

    /**
     * 新会员奖励
     */
    private int newReward;

    /**
     * 新会员奖励模式
     */
    private int rewardModel;

    /**
     * 奖励数量
     */
    private int rewardNumber;

    /**
     * 换算金额
     */
    private long convertAmount;

    /**
     * 换算积分
     */
    private long convertIntegral;
}
