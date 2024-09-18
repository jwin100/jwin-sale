package com.mammon.member.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberTagMapEntity {

    private String id;

    private String memberId;

    private String tagId;

    private LocalDateTime createTime;
}
