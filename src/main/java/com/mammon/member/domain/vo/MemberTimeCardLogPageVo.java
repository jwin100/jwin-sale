package com.mammon.member.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberTimeCardLogPageVo {

    private String id;

    private String memberId;

    /**
     *
     */
    private int changeType;

    private String changeTypeName;

    /**
     * 变更前
     */
    private long changeBefore;

    /**
     * 变更数量
     */
    private long changeTotal;

    /**
     * 变更后
     */
    private long changeAfter;

    private String remark;

    private LocalDateTime createTime;
}
