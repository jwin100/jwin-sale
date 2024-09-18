package com.mammon.stock.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StockPurchaseRefundQuery extends PageQuery {

    private Long storeNo;

    private String refundNo;

    private Integer status;
}
