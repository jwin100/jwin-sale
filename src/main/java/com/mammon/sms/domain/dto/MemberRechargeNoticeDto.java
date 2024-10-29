package com.mammon.sms.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/10/29 18:26
 */
@Data
public class MemberRechargeNoticeDto {
    private String memberId;

    /**
     * 储值金额
     */
    private long changeRecharge;

    /**
     * 储值后金额
     */
    private long afterRecharge;
}
