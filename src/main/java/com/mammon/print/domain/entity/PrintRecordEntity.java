package com.mammon.print.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 打印记录
 */
@Data
public class PrintRecordEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    /**
     * 模板id
     */
    private String templateId;

    /**
     * 终端id
     */
    private String terminalId;

    /**
     * 打印类型
     */
    private Integer printType;

    /**
     * 打印内容(这个是发送的指令，存String
     */
    private String content;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 打印状态(等待打印，已提交，提交失败，打印成功，打印失败
     *
     * @see com.mammon.print.domain.enums.PrintRecordStatusConst
     */
    private int status;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
