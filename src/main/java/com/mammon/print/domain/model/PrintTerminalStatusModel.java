package com.mammon.print.domain.model;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/2/27 15:23
 */
@Data
public class PrintTerminalStatusModel {

    /**
     * 终端号
     */
    private String terminalCode;

    private int status;
}
