package com.mammon.member.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/6/20 11:49
 */
@Data
public class MemberAssetsRechargeDto {

    private long merchantNo;

    private long storeNo;

    private String accountId;

    private String memberId;

    private String orderNo;
}
