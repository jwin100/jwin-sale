package com.mammon.member.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-04-04 11:27:39
 */
@Data
public class MemberAssetsLogEntity {

    private String id;

    private String memberId;

    /**
     * 变更订单号
     */
    private String orderNo;

    /**
     * 财产类型(积分变更，储值金额变更)
     */
    private int type;

    /**
     * 变更种类(会员储值，储值退款，会员消费，消费退款)
     */
    private int category;

    /**
     * 变更前余额
     */
    private long beforeAssets;

    /**
     * 变更余额
     */
    private long changeAssets;

    /**
     * 变更后余额
     */
    private long afterAssets;

    private String remark;

    private LocalDateTime createTime;
}