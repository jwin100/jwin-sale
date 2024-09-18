package com.mammon.stock.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StockPurchaseRefundSkuVo {

    private String id;

    private String refundId;

    private String spuId;

    private String skuId;

    /**
     * 商品名
     */
    private String skuName;

    private String unitId;
    private String unitName;

    /**
     * 采购数量
     */
    private BigDecimal purchaseQuantity;

    /**
     * 采购价格
     */
    private BigDecimal purchaseAmount;

    /**
     * 退货数量
     */
    private BigDecimal refundQuantity;

    /**
     * 退货单价
     */
    private BigDecimal refundAmount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
