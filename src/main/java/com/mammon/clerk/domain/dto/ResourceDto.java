package com.mammon.clerk.domain.dto;

import lombok.Data;

@Data
public class ResourceDto {
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源标识符
     */
    private String uri;

    /**
     * 所属父级资源
     */
    private String pid;
    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private int sort;

    /**
     * 类型,0:菜单,1:目录,2:按钮
     */
    private int resourceType;

    /**
     * 接口path
     */
    private String apiPath;

    /**
     * 页面控制按钮标识(模块:菜单:按钮) 页面按钮和标识比对，看是否显示
     */
    private String permissions;

    private int status;
}
