package com.mammon.stock.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StockIoType implements IEnum<StockIoType> {
    IN(1, "入库"),
    OUT(2, "出库"),
    ;

    private final int code;
    private final String name;

}
