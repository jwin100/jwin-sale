package com.mammon.summary.domain.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author dcl
 * @since 2024/2/29 10:33
 */
@Data
public class SummaryCashierDto {

    private LocalDate summaryDate;

    private int cashierTotal;

    private long cashierAmount;
}
