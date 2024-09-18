package com.mammon.stock.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-06-08 15:40:11
 */
@Data
public class StockSkuLockEntity {

    private String id;

    private String spuId;

    private String skuId;

    private long merchantNo;

    private long storeNo;

    private String orderId;

    private long lockStock;

    private LocalDateTime createTime;
}
