package com.mammon.office.edition.domain.enums;

import com.mammon.enums.IEnum;
import lombok.Getter;

/**
 * @author dcl
 * @date 2023-03-06 10:53:09
 */
@Getter
public enum IndustryUnit implements IEnum<IndustryUnit> {

    year(1, "年"),
    month(2, "月"),
    twig(3, "条"),
    ;

    private int code;

    private String name;

    private IndustryUnit(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
