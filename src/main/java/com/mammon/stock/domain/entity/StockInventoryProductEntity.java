package com.mammon.stock.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 盘点商品明细
 *
 * @author dcl
 * @since 2024/4/1 16:16
 */
@Data
public class StockInventoryProductEntity {

    private String id;

    private String inventoryId;

    private String spuId;

    private String spuName;

    private String categoryId;

    private String categoryName;

    /**
     * 盘点前库存
     */
    private long beforeStock;

    /**
     * 实盘数量
     */
    private long realityStock;

    /**
     * 盈亏数量
     */
    private long phaseStock;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
