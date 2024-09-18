package com.mammon.member.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberTagEntity {

    private String id;

    private long merchantNo;

    private String name;

    private String color;

    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
