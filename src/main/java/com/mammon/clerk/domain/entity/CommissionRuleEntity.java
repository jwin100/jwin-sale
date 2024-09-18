package com.mammon.clerk.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/4/7 15:40
 */
@Data
public class CommissionRuleEntity {

    private String id;

    private long merchantNo;

    /**
     * 类型
     */
    private int type;

    /**
     * 计算模式
     */
    private int mode;

    /**
     * 计算值单位
     */
    private int unit;

    /**
     * 提成值
     */
    private double rate;

    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
