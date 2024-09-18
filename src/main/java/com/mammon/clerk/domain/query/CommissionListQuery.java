package com.mammon.clerk.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * @author dcl
 * @since 2024/4/8 10:09
 */
@Data
public class CommissionListQuery {

    private List<String> accountIds;

    private LocalDate commissionStartTime;

    private LocalDate commissionEndTime;
}
