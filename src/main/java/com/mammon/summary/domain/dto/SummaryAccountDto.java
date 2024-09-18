package com.mammon.summary.domain.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 店员业绩统计
 *
 * @author dcl
 * @since 2024/4/22 14:03
 */
@Data
public class SummaryAccountDto {
    /**
     * 统计日期
     */
    private LocalDate summaryDate;

    /**
     * 店员id
     */
    private String accountId;

    /**
     * 总金额
     */
    private long totalAmount;

    /**
     * 销售金额
     */
    private long cashierAmount;

    /**
     * 储值金额
     */
    private long rechargeAmount;

    /**
     * 开卡金额
     */
    private long countedAmount;

    /**
     * 服务金额
     */
    private long serviceAmount;
}
