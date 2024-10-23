package com.mammon.goods.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/1/15 11:26
 */
@Getter
@AllArgsConstructor
public enum SpuCountedType implements IEnum<SpuCountedType> {
    NO(0, "否"),
    YES(1, "是"),
    ;

    private final int code;
    private final String name;
}
