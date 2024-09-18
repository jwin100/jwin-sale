package com.mammon.sms.domain.dto;

import lombok.Data;

@Data
public class SmsTempDto {

    /**
     * 模板类型
     */
    private int tempType;

    /**
     * 模板名
     */
    private String tempName;

    private String template;
}
