package com.mammon.office.order.domain.enums;

import com.mammon.enums.IEnum;
import lombok.Getter;

/**
 * @author dcl
 * @date 2023-03-05 20:39:29
 */
@Getter
public enum OfficeOrderSource implements IEnum<OfficeOrderSource> {
    web(1, "web"),
    ;

    private int code;

    private String name;

    private OfficeOrderSource(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
