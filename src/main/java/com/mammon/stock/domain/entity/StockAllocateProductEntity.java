package com.mammon.stock.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockAllocateProductEntity {

    private String id;

    private String allocateNo;

    private String spuId;

    private String skuId;

    /**
     * 商品名
     */
    private String skuName;

    private String unitId;
    private String unitName;

    /**
     * 调拨数量
     */
    private long allocateQuantity;

    /**
     * 实际出库数量
     */
    private long outQuantity;

    /**
     * 实际入库数量
     */
    private long inQuantity;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
