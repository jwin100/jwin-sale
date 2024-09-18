package com.mammon.print.domain.vo;

import com.mammon.print.domain.enums.PrintTemplateType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PrintTemplateVo {

    private String id;

    /**
     * merchantNo为0的为默认模板，
     * 当商户模板为空情况获取默认模板打印
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

    /**
     * 模板内容(json
     */
    private List<TemplateModuleVo> templateModules = new ArrayList<>();

    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
