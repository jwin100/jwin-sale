package com.mammon.office.order.domain.enums;

import com.mammon.enums.IEnum;
import lombok.Getter;

/**
 * @author dcl
 * @date 2023-03-06 14:17:03
 */
@Getter
public enum OfficeOrderRefundStatus implements IEnum<OfficeOrderRefundStatus> {

    refundApply(1, "待审核"),
    refunding(2, "退款中"),
    refundSuccess(3, "退款成功"),
    refundCancel(4, "退款关闭"),
    ;

    private int code;

    private String name;

    private OfficeOrderRefundStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
