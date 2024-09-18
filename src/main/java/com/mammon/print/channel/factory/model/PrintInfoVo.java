package com.mammon.print.channel.factory.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/26 20:10
 */
@Data
public class PrintInfoVo {

    /**
     * 版本号
     */
    private String version;

    /**
     * 宽度
     */
    private int width;
}
