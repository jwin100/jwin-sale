package com.mammon.clerk.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @since 2024/4/7 17:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommissionRuleQuery extends PageQuery {

    private Integer status;
}
