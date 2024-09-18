package com.mammon.clerk.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author dcl
 * @since 2024/4/8 10:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommissionQuery extends PageQuery {

    private Long storeNo;

    private LocalDate commissionStartTime;

    private LocalDate commissionEndTime;
}
