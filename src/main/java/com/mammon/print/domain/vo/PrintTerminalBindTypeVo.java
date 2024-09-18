package com.mammon.print.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.print.domain.enums.PrintTemplateType;
import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/26 16:31
 */
@Data
public class PrintTerminalBindTypeVo {

    private int type;

    private String typeName;

    public String getTypeName() {
        return IEnum.getNameByCode(this.getType(), PrintTemplateType.class);
    }
}
