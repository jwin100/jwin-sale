package com.mammon.biz.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/10/16 10:27
 */
@Data
public class ActionLogDto {

    /**
     * 位置，1:头部，2目录，3：页面
     */
    private int position;

    /**
     * 事件
     */
    private String event;

    /**
     * 操作平台
     */
    private int source;
}
