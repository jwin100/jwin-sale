package com.mammon.goods.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品标签
 */
@Data
public class TagEntity {

    private String id;

    private long merchantNo;

    private String name;

    private String remark;

    private int sort;

    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}