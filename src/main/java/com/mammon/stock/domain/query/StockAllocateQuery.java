package com.mammon.stock.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StockAllocateQuery extends PageQuery {

    private String searchKey;

    private Long storeNo;

    private String allocateNo;

    private Integer status;
}
