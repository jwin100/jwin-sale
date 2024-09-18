package com.mammon.member.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员等级规则
 */
@Data
public class MemberLevelEntity {

    private String id;

    private long merchantNo;

    private String name;

    private long startIntegral;

    private long endIntegral;

    private long discount;

    private LocalDateTime createTime;
}
