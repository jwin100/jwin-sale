package com.mammon.cashier.domain.query;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author dcl
 * @since 2024/2/21 11:45
 */
@Data
public class CashierRefundSummaryQuery {

    private long merchantNo;

    private Long storeNo;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer category;

    private Integer status;

    private String memberId;
}
