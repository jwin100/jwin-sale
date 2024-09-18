package com.mammon.clerk.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountLoginLogEntity {

    private String id;

    private String accountId;

    private int type;

    private LocalDateTime loginTime;

    private String ip;

    private int platform;

    private String address;

    private String userAgent;

    private LocalDateTime createTime;
}
