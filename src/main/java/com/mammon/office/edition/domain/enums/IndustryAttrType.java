package com.mammon.office.edition.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2023/8/3 10:07
 */
@Getter
@AllArgsConstructor
public enum IndustryAttrType implements IEnum<IndustryAttrType> {
    GOODS(1, "商品管理"),
    STOCK(2, "库存管理"),
    MEMBER(3, "会员管理"),
    CASHIER(4, "收银记账"),
    ORDER(5, "订单管理"),
    STAFF(6, "店员管理"),
    STORE(7, "店务管理"),
    ;

    private final int code;
    private final String name;
}
