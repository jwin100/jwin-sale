package com.mammon.biz.domain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 行为记录
 *
 * @author dcl
 * @since 2024/10/16 16:16
 */
@Data
public class ActionLogEntity {

    private String id;

    /**
     * 商户号
     */
    private long merchantNo;

    /**
     * 门店号
     */
    private long storeNo;

    /**
     * 操作人
     */
    private String accountId;

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
     *
     * @see com.mammon.enums.CommonSource
     */
    private int source;

    private LocalDateTime createTime;
}
