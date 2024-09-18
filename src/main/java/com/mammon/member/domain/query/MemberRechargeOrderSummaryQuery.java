package com.mammon.member.domain.query;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author dcl
 * @since 2024/2/21 17:55
 */
@Data
public class MemberRechargeOrderSummaryQuery {

    private long merchantNo;

    private Long storeNo;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer refunded;
}
