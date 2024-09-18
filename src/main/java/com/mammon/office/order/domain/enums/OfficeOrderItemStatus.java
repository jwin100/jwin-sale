package com.mammon.office.order.domain.enums;

import com.mammon.enums.IEnum;
import lombok.Getter;

/**
 * @author dcl
 * @date 2023-03-06 09:51:36
 */
@Getter
public enum OfficeOrderItemStatus implements IEnum<OfficeOrderItemStatus> {

    waitActive(1, "待生效"),
    activeSuccess(2, "已生效"),
    activeFail(3, "生效失败"),
    callbackSuccess(4, "已退还"),
    callbackFail(5, "退还失败")
    ;

    private int code;

    private String name;

    private OfficeOrderItemStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
