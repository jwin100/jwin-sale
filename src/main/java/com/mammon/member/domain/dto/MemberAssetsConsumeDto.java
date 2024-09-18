package com.mammon.member.domain.dto;

import lombok.Data;

/**
 * 会员金额变动信息
 *
 * @author dcl
 * @since 2024/6/20 10:56
 */
@Data
public class MemberAssetsConsumeDto {

    private long merchantNo;

    private long storeNo;

    private String accountId;

    private String memberId;

    private String orderNo;

    private long changeAmount;
}
