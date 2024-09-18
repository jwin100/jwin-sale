package com.mammon.document.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 目录
 *
 * @author dcl
 * @since 2024/8/2 11:54
 */
@Data
public class DocumentResourceEntity {

    private String id;

    private String pid;

    /**
     * 目录名
     */
    private String name;

    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
