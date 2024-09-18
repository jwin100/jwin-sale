package com.mammon.member.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员删除记录
 */
@Data
public class MemberDelRecordEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    /**
     * 会员信息
     */
    private String member;

    /**
     * 操作人
     */
    private String accountId;

    /**
     * 删除日期
     */
    private LocalDateTime createTime;
}
