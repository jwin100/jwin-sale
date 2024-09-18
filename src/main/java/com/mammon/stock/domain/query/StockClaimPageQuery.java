package com.mammon.stock.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @since 2024/3/12 16:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StockClaimPageQuery extends PageQuery {

    private String largeKeyword;

    private String largeUnitId;

    private String smallKeyword;

    private String smallUnitId;

    private Integer status;
}
