package com.mammon.cashier.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠计算方式，1折扣，2积分抵扣，3优惠券抵扣，4抹零，5加价减价
 */
@Data
public class CashierRefundComputeVo {
    /**
     * 原价(调整前金额)
     */
    private BigDecimal originalAmount;

    /**
     * 调整金额(手动加减价)
     */
    private BigDecimal adjustAmount;

    /**
     * 应收=原价-优惠金额
     */
    private BigDecimal payableAmount;

    /**
     * 应退积分
     */
    private long refundIntegral;

    private List<CashierRefundSkuComputeVo> skus = new ArrayList<>();
}
