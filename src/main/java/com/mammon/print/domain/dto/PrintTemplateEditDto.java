package com.mammon.print.domain.dto;

import com.mammon.print.domain.enums.PrintTemplateType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PrintTemplateEditDto {
    /**
     * 模板类型
     *
     * @see PrintTemplateType
     */
    private int type;

    /**
     * 模板内容(json
     */
    private List<PrintTemplateSettingDto> modules = new ArrayList<>();
}
