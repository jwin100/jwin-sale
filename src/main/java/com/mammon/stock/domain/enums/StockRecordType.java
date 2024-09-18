package com.mammon.stock.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/1/17 15:39
 */
@Getter
@AllArgsConstructor
public enum StockRecordType implements IEnum<StockRecordType> {
    PURCHASE_ORDER(1, StockIoType.IN.getCode(), "采购订单入库"),
    PURCHASE_REFUND(2, StockIoType.OUT.getCode(), "采购退货出库"),
    ALLOCATE_IN(3, StockIoType.IN.getCode(), "调拨入库"),
    ALLOCATE_OUT(4, StockIoType.OUT.getCode(), "调拨出库"),
    CLAIM_IN(5, StockIoType.IN.getCode(), "组装拆包入库"),
    CLAIM_OUT(6, StockIoType.OUT.getCode(), "组装拆包出库"),
    OTHER_IN(998, StockIoType.IN.getCode(), "其他入库"),
    OTHER_OUT(999, StockIoType.OUT.getCode(), "其他出库"),
    ;

    private final int code;
    private final int ioType;
    private final String name;
}
