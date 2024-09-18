package com.mammon.stock.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 出入库记录详情
 */
@Data
public class StockRecordSkuEntity {

    private String id;

    private String recordNo;

    private String spuId;

    private String skuId;

    private String skuName;

    /**
     * 操作数量
     */
    private long recordQuantity;

    /**
     * 操作价格
     */
    private long recordAmount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
