package com.mammon.member.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-04-04 11:11:43
 */
@Data
public class MemberAssetsEntity {

    /**
     * 会员id
     */
    private String id;

    /**
     * 当前积分
     */
    private long nowIntegral;

    /**
     * 累计积分
     */
    private long accrualIntegral;

    /**
     * 当前充值金额
     */
    private long nowRecharge;

    /**
     * 累计充值金额
     */
    private long accrualRecharge;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
