package com.mammon.clerk.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 角色信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {
    private String id;

    private long merchantNo;

    private String name;
    private String enName;
    private String remark;

    /**
     * 默认角色
     */
    private int defaultStatus;

    /**
     * 权限类型
     */
    private int type;

    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
