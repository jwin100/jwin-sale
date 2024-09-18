package com.mammon.stock.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StockPurchaseOrderQuery extends PageQuery {

    private Long storeNo;

    private String purchaseNo;

    private Integer status;
}
