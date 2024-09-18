package com.mammon.stock.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StockPurchaseOrderSkuVo {

    private String id;

    private String purchaseId;

    private String spuId;

    private String skuId;

    /**
     * 商品名
     */
    private String skuName;

    private BigDecimal sellStock;

    private String unitId;
    private String unitName;

    /**
     * 零售价
     */
    private BigDecimal referenceAmount;

    /**
     * 采购数量
     */
    private BigDecimal purchaseQuantity;

    /**
     * 采购价格
     */
    private BigDecimal purchaseAmount;

    /**
     * 入库数量
     */
    private BigDecimal replenishQuantity;

    /**
     * 退货数量
     */
    private BigDecimal refundQuantity;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
