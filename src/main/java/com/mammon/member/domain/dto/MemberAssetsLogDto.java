package com.mammon.member.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/10/16 17:20
 */
@Data
public class MemberAssetsLogDto {
    private String memberId;
    private int type;
    private int category;
    private String orderNo;
    private long beforeAssets;
    private long changeAssets;
    private long afterAssets;
    private String remark;
}
