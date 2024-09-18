package com.mammon.clerk.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/4/7 17:09
 */
@Data
public class CommissionRuleDto {

    /**
     * 类型
     */
    private int type;

    /**
     * 计算模式
     */
    private int mode;

    private int unit;

    /**
     * 提成值
     */
    private double rate;
}
