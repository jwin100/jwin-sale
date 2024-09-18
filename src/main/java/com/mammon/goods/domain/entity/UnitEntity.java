package com.mammon.goods.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 单位
 */
@Data
public class UnitEntity {

    private String id;

    private long merchantNo;

    private String name;

    /**
     * 单位计价方式
     */
    private int type;

    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
