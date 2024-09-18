package com.mammon.member.domain.dto;

import lombok.Data;

@Data
public class MemberLevelDto {
    private String name;

    private long startIntegral;

    private long endIntegral;

    private long discount;
}
