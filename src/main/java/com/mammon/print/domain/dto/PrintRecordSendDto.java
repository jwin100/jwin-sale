package com.mammon.print.domain.dto;

import com.mammon.print.domain.enums.PrintTemplateType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PrintRecordSendDto {

    /**
     * @see PrintTemplateType
     */
    private int type;

    /**
     * 设备id
     */
    private String terminalId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 打印内容
     */
    @NotNull(message = "打印内容不能为空")
    private PrintActiveDto contents;
}
