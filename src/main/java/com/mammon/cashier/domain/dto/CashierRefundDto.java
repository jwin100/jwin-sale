package com.mammon.cashier.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CashierRefundDto {

    /**
     * 方式重复提交
     */
    private long requestId;

    private String orderId;

    /**
     * 订单来源
     */
    private int source;

    /**
     * 退款原因
     */
    private int reason;

    /**
     * 整单备注
     */
    private String remark;

    /**
     * 调整金额
     */
    private BigDecimal adjustAmount;

    private long countedTotal;

    /**
     * 退货信息
     */
    private List<CashierRefundSkuDto> skus = new ArrayList<>();
}