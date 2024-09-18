package com.mammon.print.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.print.domain.enums.PrintTemplateClassify;
import com.mammon.print.domain.enums.PrintTemplateType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @since 2023/8/24 11:26
 */
@Data
public class PrintTemplateListVo {

    private int type;

    private String typeName;

    private int classify;

    private String classifyName;

    /**
     * 模板描述
     */
    private String remark;

    public String getTypeName() {
        return IEnum.getNameByCode(this.getType(), PrintTemplateType.class);
    }

    public String getClassifyName() {
        return IEnum.getNameByCode(this.getClassify(), PrintTemplateClassify.class);
    }
}
