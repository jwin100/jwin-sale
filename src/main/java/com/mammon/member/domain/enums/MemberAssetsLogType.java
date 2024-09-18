package com.mammon.member.domain.enums;

import com.mammon.enums.IEnum;
import lombok.Getter;

/**
 * @author dcl
 * @date 2023-04-04 11:34:06
 */
@Getter
public enum MemberAssetsLogType implements IEnum<MemberAssetsLogType> {
    CHANGE_INTEGRAL(1, "积分变更"),
    CHANGE_RECHARGE(2, "储值变更"),
    ;

    private final int code;
    private final String name;

    MemberAssetsLogType(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
