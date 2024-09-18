package com.mammon.market.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @since 2023/7/17 16:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MarketTimeCardRuleQuery extends PageQuery {

    private Integer deleted;
}
