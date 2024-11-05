package com.mammon.member.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/11/3 13:28
 */
@Data
public class MemberTimeCardLogDto {
    private String countedId;
    private String accountId;
    private String memberId;
    private String orderNo;
    private int changeType;
    private long timeTotal;
    private long changeAfter;
    private String remark;
    private String timeCardName;
}
