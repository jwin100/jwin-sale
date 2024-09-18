package com.mammon.stock.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StockRecordQuery extends PageQuery {

    private Long storeNo;

    private Integer ioType;
}
