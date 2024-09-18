package com.mammon.summary.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 销售统计
 *
 * @author dcl
 * @since 2024/2/29 13:30
 */
@Data
public class SummaryCashierVo {

    /**
     * 统计日期
     */
    private LocalDate summaryDate;

    /**
     * 销售单数
     */
    private int cashierTotal;

    /**
     * 销售总额
     */
    private BigDecimal cashierAmount;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
}
