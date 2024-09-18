package com.mammon.office.edition.domain.enums;

import com.mammon.enums.IEnum;
import lombok.Getter;

/**
 * @author dcl
 * @date 2023-03-14 11:48:16
 */
@Getter
public enum IndustryType implements IEnum<IndustryType> {

    major_edition(1, "版本"),
    flagship_edition(2, "版本"),
    sms(3, "短信"),
    storeQuota(4, "门店额度"),
    ;

    private int code;

    private String name;

    IndustryType(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
