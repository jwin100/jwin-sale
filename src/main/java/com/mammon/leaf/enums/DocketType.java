package com.mammon.leaf.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/1/25 13:28
 */
@Getter
@AllArgsConstructor
public enum DocketType implements IEnum<DocketType> {
    CASHIER_ORDER(1, "S", "销售订单"),
    CASHIER_REFUND(2, "T", "销售退货"),
    CASHIER_HANG(3, "H", "挂单"),
    PURCHASE_ORDER(4, "PS", "采购订单"),
    PURCHASE_REFUND(5, "PT", "采购退货"),
    ALLOCATE(6, "PA", "调拨单"),
    STOCK_RECORD(7, "R", "库存明细"),
    GOODS_SPU_CODE(8, "M", "商品编码"),
    GOODS_SKU_CODE(9, "M", "规格编码"),
    PAYMENT_TRADE(10, "", "支付中心流水号"),
    PAYMENT_TRADE_REFUND(11, "", "支付中心退款流水号"),
    STOCK_INVENTORY(12, "P", "门店盘点号"),
    ;

    private final int code;
    private final String prefix;
    private final String name;
}
