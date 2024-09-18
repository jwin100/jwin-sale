package com.mammon.print.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 打印模板宽度规格
 *
 * @author dcl
 * @since 2024/3/27 14:56
 */
@Getter
@AllArgsConstructor
public enum PrintTemplateWidth implements IEnum<PrintTemplateWidth> {
    WIDTH_58(58, PrintTemplateClassify.TICKET, "58mm"),
    WIDTH_80(80, PrintTemplateClassify.TICKET, "80mm"),
    WIDTH_30(30, PrintTemplateClassify.PRICE_TAG, "50*30mm"),
    WIDTH_40(40, PrintTemplateClassify.PRICE_TAG, "70*40mm"),
    ;

    private int code;
    private final PrintTemplateClassify classify;
    private final String name;
}
