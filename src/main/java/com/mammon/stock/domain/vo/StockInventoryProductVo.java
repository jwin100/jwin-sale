package com.mammon.stock.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/4/1 17:20
 */
@Data
public class StockInventoryProductVo {

    private String id;

    private String inventoryId;

    private String spuId;

    private String spuName;

    private String categoryId;

    private String categoryName;

    /**
     * 盘点前库存
     */
    private BigDecimal beforeStock;

    /**
     * 实盘数量
     */
    private BigDecimal realityStock;

    /**
     * 盈亏数量
     */
    private BigDecimal phaseStock;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
