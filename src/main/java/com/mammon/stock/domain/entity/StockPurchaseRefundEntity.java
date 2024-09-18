package com.mammon.stock.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockPurchaseRefundEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String refundNo;

    /**
     * 采购单号
     */
    private String purchaseId;

    /**
     * 采购状态
     */
    private int status;

    private String errDesc;

    /**
     * 操作人
     */
    private String operationId;

    private String remark;

    private String reasonId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
