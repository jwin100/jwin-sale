package com.mammon.goods.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/1/17 10:04
 */
@Getter
@AllArgsConstructor
public enum CategoryLevel implements IEnum<CategoryLevel> {
    ONE(1, "一级分类"),
    TWO(2, "二级分类"),
    ;

    private final int code;
    private final String name;
}
