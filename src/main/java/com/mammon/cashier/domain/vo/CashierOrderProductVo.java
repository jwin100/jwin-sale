package com.mammon.cashier.domain.vo;

import com.mammon.goods.domain.dto.SkuSpecDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CashierOrderProductVo {

    private String id;

    private String orderId;


    private String spuId;


    private String skuId;

    /**
     * 商品名
     */
    private String skuName;

    private String picture;

    /**
     * 数量
     */
    private BigDecimal saleQuantity;

    /**
     * 零售价
     */
    private BigDecimal referenceAmount;

    /**
     * 调整单价
     */
    private BigDecimal adjustAmount;

    /**
     * 应收
     */
    private BigDecimal payableAmount;

    /**
     * 分摊后金额(整单优惠分摊后)
     */
    private BigDecimal divideAmount;

    /**
     * 退货数量
     */
    private BigDecimal refundQuantity;

    private BigDecimal refundAmount;

    private int status;

    private String statusName;

    private String unitId;

    private String unitName;

    private long unitType;
}
