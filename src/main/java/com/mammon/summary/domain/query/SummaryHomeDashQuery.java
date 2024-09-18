package com.mammon.summary.domain.query;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author dcl
 * @since 2024/2/21 11:45
 */
@Data
public class SummaryHomeDashQuery {

    /**
     * 查看所有门店
     */
    private boolean allStore;

    private Long storeNo;

    private LocalDate startDate;

    private LocalDate endDate;
}
