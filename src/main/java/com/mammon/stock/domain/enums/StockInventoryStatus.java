package com.mammon.stock.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/4/1 17:14
 */
@Getter
@AllArgsConstructor
public enum StockInventoryStatus implements IEnum<StockInventoryStatus> {
    CREATE(1, "创建"),
    INVENTORYING(2, "盘点中"),
    INVENTORY_END(3, "盘点结束"),
    INVENTORY_CANCEL(4, "已取消"),
    ;

    private final int code;
    private final String name;
}
