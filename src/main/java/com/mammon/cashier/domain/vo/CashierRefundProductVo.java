package com.mammon.cashier.domain.vo;

import com.mammon.goods.domain.dto.SkuSpecDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CashierRefundProductVo {

    private String id;

    private String refundId;

    private String orderProductId;

    private String spuId;

    private String skuId;

    /**
     * 商品名
     */
    private String skuName;

    /**
     * 商品图
     */
    private String picture;

    /**
     * 退货数量
     */
    private BigDecimal refundQuantity;

    /**
     * 原单价
     */
    private BigDecimal referenceAmount;

    /**
     * 调整单价金额
     */
    private BigDecimal adjustAmount;

    /**
     * 应退
     */
    private BigDecimal payableAmount;

    private int status;

    private String unitId;

    private String unitName;

    private long unitType;
}
