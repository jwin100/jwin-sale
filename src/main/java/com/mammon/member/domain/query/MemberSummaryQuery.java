package com.mammon.member.domain.query;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author dcl
 * @since 2024/2/21 17:09
 */
@Data
public class MemberSummaryQuery {

    private long merchantNo;

    private Long storeNo;

    private LocalDate startDate;

    private LocalDate endDate;
}
