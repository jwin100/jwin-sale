package com.mammon.goods.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2023/12/7 14:28
 */
@Getter
@AllArgsConstructor
public enum SpuStatus implements IEnum<SpuStatus> {
    ENABLE(1, "上架"),
    DISABLE(2, "下架"),
    ;

    private final int code;
    private final String name;
}
