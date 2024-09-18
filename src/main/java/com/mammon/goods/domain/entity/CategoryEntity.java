package com.mammon.goods.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 分类
 */
@Data
public class CategoryEntity {

    private String id;

    private long merchantNo;

    private String name;

    private String pid;

    private int level;

    private int sort;

    private int status;

    /**
     * 是否删除（0：否，1：是）
     */
    private int deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
