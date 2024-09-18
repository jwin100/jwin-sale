package com.mammon.summary.domain.query;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author dcl
 * @since 2024/2/29 10:10
 */
@Data
public class SummaryHomeTrendQuery {

    private LocalDate startDate;

    private LocalDate endDate;
}
