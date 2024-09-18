package com.mammon.stock.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @since 2024/4/1 18:23
 */
@Data
public class StockInventoryProductDto {

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
}
