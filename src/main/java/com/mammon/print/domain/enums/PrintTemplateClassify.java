package com.mammon.print.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2023/8/24 10:30
 */
@Getter
@AllArgsConstructor
public enum PrintTemplateClassify implements IEnum<PrintTemplateClassify> {
    TICKET(1, "小票"),
    PRICE_TAG(2, "价签"),
    BAR_CODE(3, "条码"),
    ;

    private final int code;
    private final String name;
}
