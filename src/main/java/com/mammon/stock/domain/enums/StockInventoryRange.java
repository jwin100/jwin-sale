package com.mammon.stock.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/4/1 16:09
 */
@Getter
@AllArgsConstructor
public enum StockInventoryRange implements IEnum<StockInventoryRange> {
    ALL(1, "全部商品"),
    CLASSIFY(2, "指定分类"),
    ;

    private final int code;
    private final String name;
}
