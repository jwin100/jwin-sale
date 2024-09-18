package com.mammon.print.domain.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/23 17:47
 */
@Data
public class PrintChannelListVo {

    private String id;


    /**
     * 渠道编号
     */
    private String channelCode;

    /**
     * 渠道名
     */
    private String channelName;

    private int classify;

    private String classifyName;
}
