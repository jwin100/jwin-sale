package com.mammon.merchant.domain.enums;

import com.mammon.enums.IEnum;
import com.mammon.merchant.domain.vo.MerchantVo;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/8/21 9:59
 */
@Getter
@AllArgsConstructor
public enum MerchantType implements IEnum<MerchantType> {
    ORDINARY(0, "普通账号"),
    EXPERIENCE(1, "体验账号"),
    ;

    private final int code;
    private final String name;
}
