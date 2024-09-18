package com.mammon.summary.domain.query;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author dcl
 * @since 2024/5/31 15:17
 */
@Data
public class SummaryStoreQuery {

    private Long storeNo;

    private LocalDate startDate;

    private LocalDate endDate;
}
