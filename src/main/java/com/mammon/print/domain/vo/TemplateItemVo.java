package com.mammon.print.domain.vo;

import lombok.Data;

@Data
public class TemplateItemVo {

    /**
     * 是否可自定义（0:不可自定义，1:可以自定义
     */
    private int itemCustomize;

    private String itemKey;

    private String itemName;

    private int itemSort;

    /**
     * 0:未选，1:已选
     */
    private Integer itemStatus;

    /**
     * 1:选择，2:输入文本。3:选图
     */
    private int itemType;

    /**
     * 文本内容（itemType(2:内容是文本，3:内容是url
     */
    private String itemValue;
}
