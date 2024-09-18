package com.mammon.feedback.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/1/16 10:46
 */
@Getter
@AllArgsConstructor
public enum FeedbackType implements IEnum<FeedbackType> {
    QUALITY(1, "质量问题"),
    DATA(2, "数据问题"),
    METHOD(3, "使用方法"),
    PROPOSE(4, "意见建议"),
    ADD_ABILITY(5, "新增功能"),
    OTHER(99, "其他"),
    ;

    private final int code;
    private final String name;
}
