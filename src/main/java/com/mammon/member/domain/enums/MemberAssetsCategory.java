package com.mammon.member.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/2/21 14:50
 */
@Getter
@AllArgsConstructor
public enum MemberAssetsCategory implements IEnum<MemberAssetsCategory> {
    RECHARGE_ORDER(1, "会员储值"),
    RECHARGE_REFUND(2, "储值退款"),
    CASHIER_ORDER(3, "会员消费"),
    CASHIER_REFUND(4, "消费退款"),
    INTEGRAL_ADD(5, "积分增加"),
    INTEGRAL_REMOVE(6, "积分减少"),
    ;

    private final int code;
    private final String name;
}
