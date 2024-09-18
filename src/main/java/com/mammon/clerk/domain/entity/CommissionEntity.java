package com.mammon.clerk.domain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/4/7 15:38
 */
@Data
public class CommissionEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    /**
     * 统计日期
     */
    private LocalDate commissionTime;

    /**
     * 店员id
     */
    private String accountId;

    /**
     * 提成模板
     */
    private String commissionRuleId;

    /**
     * 提成总额
     */
    private long totalAmount;

    /**
     * 销售提成
     */
    private long cashierAmount;

    /**
     * 储值提成
     */
    private long rechargeAmount;

    /**
     * 计次提成
     */
    private long countedAmount;

    /**
     * 服务提成
     */
    private long serviceAmount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
