package com.mammon.summary.domain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 销售统计
 *
 * @author dcl
 * @since 2024/2/29 13:30
 */
@Data
public class SummaryCashierEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

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
    private long cashierAmount;

    /**
     * 退款单数
     */
    private int refundTotal;

    /**
     * 退款金额
     */
    private long refundAmount;

    /**
     * 统计日期
     */
    private LocalDateTime createTime;
}
