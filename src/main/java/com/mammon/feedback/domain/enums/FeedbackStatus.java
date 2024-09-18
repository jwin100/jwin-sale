package com.mammon.feedback.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/1/16 10:25
 */
@Getter
@AllArgsConstructor
public enum FeedbackStatus implements IEnum<FeedbackStatus> {
    HANDLE_WAIT(0, "待处理"),
    HANDLE_ING(1, "处理中"),
    HANDLE_FINISH(2, "处理完成"),
    ;

    private final int code;
    private final String name;
}
