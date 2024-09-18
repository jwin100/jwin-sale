package com.mammon.print.domain.entity;

import lombok.Data;

/**
 * 模板分类和厂商对应关系
 * <p>
 * 一个厂商对应多种模板分类（一个厂商有多重打印类型的打印机）
 *
 * @author dcl
 * @since 2024/3/27 15:31
 */
@Data
public class PrintChannelMapEntity {

    private String id;

    private int classify;

    private String channelId;
}
