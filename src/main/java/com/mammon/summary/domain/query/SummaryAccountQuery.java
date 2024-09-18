package com.mammon.summary.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author dcl
 * @since 2024/5/31 11:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SummaryAccountQuery extends PageQuery {

    /**
     * 只看我的
     */
    private boolean lookMy;

    private Long storeNo;

    private LocalDate startDate;

    private LocalDate endDate;
}
