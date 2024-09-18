package com.mammon.member.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberSex implements IEnum<MemberSex> {
    MAN(1,"男"),
    WOMAN(2,"女"),
    ;

    private final int code;
    private final String name;
}
