package com.mammon.office.edition.domain.entity;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-02-02 11:05:44
 * <p>
 * 版本功能表
 */
@Data
public class IndustryAttrEntity {

    private String id;

    /**
     * 功能名称
     */
    private String name;

    private int type;

    private String perm;

    private int sort;

    /**
     * 免费版是否可用
     */
    private boolean fee;
    /**
     * 状态(1:可用，2：不可用)
     */
    private int status;
}
