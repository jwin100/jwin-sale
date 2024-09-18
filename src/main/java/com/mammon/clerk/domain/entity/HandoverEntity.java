package com.mammon.clerk.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 交班记录
 *
 * @author dcl
 * @since 2023/9/28 13:54
 */
@Data
public class HandoverEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String accountId;

    /**
     * 1接班，2：交班
     */
    private int type;

    private LocalDateTime handoverTime;
}
