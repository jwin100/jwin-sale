package com.mammon.sms.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/10/29 18:26
 */
@Data
public class MemberCountedNoticeDto {
    private String memberId;
    private String timeCardName;
    private int addTimeCard;
    private int nowTimeCard;
}
