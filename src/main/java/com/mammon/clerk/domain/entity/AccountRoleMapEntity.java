package com.mammon.clerk.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountRoleMapEntity {

    private String id;

    private String accountId;

    private String roleId;

    private LocalDateTime createTime;
}
