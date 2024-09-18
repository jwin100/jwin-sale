package com.mammon.stock.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 采购单
 * 创建采购单
 * 采购入库,或者不根据采购单入库、生成临时
 */
@Data
public class StockPurchaseOrderEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    /**
     * 采购单号
     */
    private String purchaseNo;

    /**
     * 采购门店
     */
    private long purchaseStoreNo;

    /**
     * 采购状态
     */
    private int status;

    /**
     * 关闭原因
     */
    private String errDesc;

    /**
     * 有退
     */
    private int refundMark;

    /**
     * 操作人
     */
    private String operationId;

    /**
     * 送达日期
     */
    private LocalDateTime deliveryTime;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
