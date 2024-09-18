package com.mammon.office.edition.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-02-02 10:55:00
 * 版本名称
 */
@Data
public class IndustryEntity {

    private String id;

    private String name;

    /**
     * 版本类型 (1:版本，2：短信，3：门店额度)
     */
    private int type;

    /**
     * 状态（1：上家，2：下架）
     */
    private int status;

    /**
     * 数量
     */
    private long quantity;

    /**
     * 单位(1:年,2:月,3:条)
     */
    private int unit;

    private LocalDateTime createTime;
}
