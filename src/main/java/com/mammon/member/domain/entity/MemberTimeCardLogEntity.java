package com.mammon.member.domain.entity;

import com.mammon.member.domain.enums.TimeCardChangeTypeConst;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 计次卡变更记录
 */
@Data
public class MemberTimeCardLogEntity {
    private String id;

    private String memberId;

    /**
     * @see TimeCardChangeTypeConst
     */
    private int changeType;

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

    /**
     * 关联id
     */
    private String orderNo;

    /**
     * 操作人
     */
    private String accountId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
