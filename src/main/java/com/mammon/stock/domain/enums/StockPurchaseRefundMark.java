package com.mammon.stock.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StockPurchaseRefundMark implements IEnum<StockPurchaseRefundMark> {
    NONE(0, "无退货"),
    REFUNDED(1, "有退货"),
    ;

    private final int code;
    private final String name;
}
