package com.mammon.member.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 计次卡
 */
@Data
public class MemberTimeCardEntity {

    private String id;

    private String memberId;

    private String timeCardId;

    private String name;

    /**
     * 有效期(0:永久有效,1:结束日期)
     */
    private int expireType;

    private LocalDateTime expireTime;

    private String spuIds;

    /**
     * 计次次数
     */
    private int nowTimeCard;

    private int totalTimeCard;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}