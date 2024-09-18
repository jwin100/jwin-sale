package com.mammon.stock.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StockRecordSkuVo {

    private String id;

    private String recordNo;

    private String spuId;

    private String skuId;

    /**
     * 商品名
     */
    private String name;

    private String unitId;
    private String unitName;

    /**
     * 操作数量
     */
    private BigDecimal recordQuantity;

    /**
     * 操作价格
     */
    private BigDecimal recordAmount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
