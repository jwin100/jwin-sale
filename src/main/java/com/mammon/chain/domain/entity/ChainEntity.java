package com.mammon.chain.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 邀请码
 */
@Data
public class ChainEntity {

    private String id;

    private String code;

    private String url;

    private String link;

    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
