package com.mammon.summary.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author dcl
 * @since 2024/2/29 13:20
 */
@Data
public class SummaryHomeTrendVo {

    private LocalDate summaryDate;

    private int cashierTotal;

    private BigDecimal cashierAmount;

    private BigDecimal refundAmount;
}
