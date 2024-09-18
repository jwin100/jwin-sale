package com.mammon.print.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 打印机服务商
 */
@Data
public class PrintChannelEntity {

    private String id;

    /**
     * 渠道编号
     */
    private String channelCode;

    /**
     * 渠道名
     */
    private String channelName;

    /**
     * 配置文件信息
     */
    private String configStr;

    private String formConfig;

    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
