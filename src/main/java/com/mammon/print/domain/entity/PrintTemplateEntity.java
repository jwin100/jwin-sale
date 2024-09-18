package com.mammon.print.domain.entity;

import com.mammon.print.domain.enums.PrintTemplateType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 打印模板
 */
@Data
public class PrintTemplateEntity {

    private String id;

    /**
     * merchantNo为0的为默认模板，
     * <p>
     * 当商户模板为空情况获取默认模板打印
     * <p>
     * 商户需要自定义模板时收费
     */
    private long merchantNo;

    private int classify;

    /**
     * 模板类型
     *
     * @see PrintTemplateType
     */
    private int type;

    /**
     * 模板内容(json
     */
    private String template;

    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
