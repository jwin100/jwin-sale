package com.mammon.clerk.domain.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author dcl
 * @since 2024/4/11 10:12
 */
@Data
public class CommissionSummaryDto {

    private long storeNo;

    private LocalDate startDate;

    private LocalDate endDate;
}
