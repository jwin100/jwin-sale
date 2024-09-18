package com.mammon.print.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.print.domain.enums.PrintTerminalStatus;
import lombok.Data;

/**
 * @author dcl
 * @since 2024/2/26 18:32
 */
@Data
public class PrintTerminalListVo {

    private String id;

    private String name;

    private String version;

    private int status;

    private String statusName;

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), PrintTerminalStatus.class);
    }
}
