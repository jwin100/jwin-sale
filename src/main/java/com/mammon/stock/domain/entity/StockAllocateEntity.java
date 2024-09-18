package com.mammon.stock.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockAllocateEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String allocateNo;

    /**
     * 调入方
     */
    private long inStoreNo;

    /**
     * 调出方
     */
    private long outStoreNo;

    /**
     * 调拨状态
     */
    private int status;

    private String errDesc;

    private String remark;

    private String operationId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
