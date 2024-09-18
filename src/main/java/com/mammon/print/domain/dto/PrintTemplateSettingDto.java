package com.mammon.print.domain.dto;

import lombok.Data;

@Data
public class PrintTemplateSettingDto {

    private String itemKey;

    /**
     * 类型(1:选择，2:文本，3:选图
     */
    private int itemType;

    /**
     * 0:未选，1:已选
     */
    private int itemStatus;

    /**
     * 文本内容
     */
    private String itemValue;
}
