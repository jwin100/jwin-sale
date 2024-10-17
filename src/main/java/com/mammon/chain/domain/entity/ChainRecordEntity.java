package com.mammon.chain.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChainRecordEntity {

    private String id;

    private String code;

    private String url;

    private String ip;

    private String refer;

    private String useragent;

    private String domain;

    private LocalDateTime createTime;
}
