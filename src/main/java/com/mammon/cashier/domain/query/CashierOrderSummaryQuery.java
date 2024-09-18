package com.mammon.cashier.domain.query;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author dcl
 * @since 2024/2/21 11:45
 */
@Data
public class CashierOrderSummaryQuery {

    private long merchantNo;

    private Long storeNo;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer category;

    private Integer status;

    private String serviceAccountId;

    private String memberId;
}
