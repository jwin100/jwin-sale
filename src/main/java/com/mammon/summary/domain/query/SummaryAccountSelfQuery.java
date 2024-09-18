package com.mammon.summary.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author dcl
 * @since 2024/5/31 11:17
 */
@Data
public class SummaryAccountSelfQuery {

    private LocalDate startDate;

    private LocalDate endDate;
}
