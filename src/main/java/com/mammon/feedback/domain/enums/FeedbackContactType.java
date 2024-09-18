package com.mammon.feedback.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/1/16 10:33
 */
@Getter
@AllArgsConstructor
public enum FeedbackContactType implements IEnum<FeedbackContactType> {
    WECHAT(1, "微信"),
    QQ(2, "QQ"),
    PHONE(3, "电话"),
    ;

    private final int code;
    private final String name;
}
