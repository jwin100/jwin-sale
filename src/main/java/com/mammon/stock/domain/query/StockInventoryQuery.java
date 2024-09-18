package com.mammon.stock.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @since 2024/4/1 17:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StockInventoryQuery extends PageQuery {

    private String inventoryNo;

    private Long storeNo;
}
