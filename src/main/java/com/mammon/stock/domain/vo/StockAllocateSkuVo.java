package com.mammon.stock.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StockAllocateSkuVo {

    private String id;

    private String allocateNo;

    private String spuId;

    private String skuId;

    private BigDecimal outSellStock;

    private BigDecimal inSellStock;

    /**
     * 商品名
     */
    private String skuName;

    private String unitId;
    private String unitName;

    /**
     * 调拨数量
     */
    private BigDecimal allocateQuantity;

    /**
     * 实际出库数量
     */
    private BigDecimal outQuantity;

    /**
     * 实际入库数量
     */
    private BigDecimal inQuantity;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
