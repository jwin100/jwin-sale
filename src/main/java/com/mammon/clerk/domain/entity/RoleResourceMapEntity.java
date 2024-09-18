package com.mammon.clerk.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoleResourceMapEntity {

    private String id;

    private String roleId;

    private String resourceId;

    private int resourceIndex;

    private LocalDateTime createTime;
}
