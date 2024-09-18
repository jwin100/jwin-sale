package com.mammon.stock.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2023/12/27 13:59
 */
@Getter
@AllArgsConstructor
public enum AllocateStatus implements IEnum<AllocateStatus> {
    WAIT_EXAMINE(1, "待审核"),
    REJECT_EXAMINE(2, "审核驳回"),
    WAIT_OUT(3, "待调出"),
    WAIT_IN(4, "待调入"),
    FINISH(5, "已完成"),
    CLOSE(6, "已关闭"),
    ;

    private final int code;
    private final String name;
}
