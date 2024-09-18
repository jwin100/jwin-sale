package com.mammon.print.domain.entity;

import com.mammon.print.domain.enums.PrintTemplateClassify;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 打印机信息
 */
@Data
public class PrintTerminalEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    /**
     * 打印机
     *
     * @see PrintTemplateClassify
     */
    private int classify;

    /**
     * 所属渠道
     */
    private String channelId;

    /**
     * 打印机名
     */
    private String name;

    /**
     * 设备唯一码(每个厂商唯一码不同，根据厂商定义码)
     */
    private String terminalCode;

    /**
     * 设备绑定配置信息
     */
    private String formConfig;

    /**
     * 打印宽度
     */
    private int width;

    /**
     * 型号
     */
    private String version;

    private String bindTypes;

    /**
     * 状态(启用几个打印几个
     */
    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
