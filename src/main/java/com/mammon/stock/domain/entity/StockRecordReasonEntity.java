package com.mammon.stock.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 出入库原因
 */
@Data
public class StockRecordReasonEntity {

    private String id;

    private long merchantNo;

    private String reasonName;

    /**
     * 0:入库，1:出库
     */
    private int ioType;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
