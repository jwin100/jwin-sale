package com.mammon.clerk.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 菜单资源信息
 */
@Data
public class ResourceEntity {

    private String id;

    /**
     * 所属父级资源
     */
    private String pid;

    /**
     * 资源名称
     */
    private String title;

    /**
     * 资源标识
     */
    private String name;

    /**
     * 资源url
     */
    private String path;

    /**
     * 进入当前目录后，菜单选中path,为空则显示path对应菜单
     */
    private String activePath;

    /**
     * 组件url
     */
    private String component;

    /**
     * 页面控制按钮标识(模块:菜单:按钮)
     */
    private String permissions;

    /**
     * 是否登录认证(1:认证，2：不认证
     */
    private int requireAuth;

    /**
     * 类型,1:菜单,2:目录,3:按钮
     */
    private int type;

    /**
     * 按钮对应目录id
     * 设置权限：如果选中的是菜单，直接关联角色和菜单id
     * 如果选中的是按钮，获取到按钮对应目录的id和角色关联
     * <p>
     * 获取已有权限：根据角色获取关联id,如果是菜单返回菜单id
     * 如果是目录，获取目录关联按钮id
     * <p>
     * 一个菜单对应多个目录，一个目录对应一个菜单
     */
    private String directoryId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private int sort;

    /**
     * 1:启用，2:禁用
     */
    private int status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修订时间
     */
    private LocalDateTime updateTime;
}
