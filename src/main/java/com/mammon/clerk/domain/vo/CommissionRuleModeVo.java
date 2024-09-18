package com.mammon.clerk.domain.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/4/8 15:03
 */
@Data
public class CommissionRuleModeVo {
    private int code;
    private String name;
    private int type;
    private int unit;
    private String unitName;
}
