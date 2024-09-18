package com.mammon.document.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/8/2 11:55
 */
@Data
public class DocumentHelpEntity {

    private String id;

    private String resourceId;

    /**
     * 内容编码
     */
    private String code;

    /**
     * 内容标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
