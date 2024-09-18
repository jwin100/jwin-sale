package com.mammon.cashier.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CashierRefundPayDto {

    private String refundId;

    /**
     * 退款方式，0：原路退回,1:指定退款方式
     */
    private int refundMode;

    private List<CashierRefundPayTypeDto> payTypes = new ArrayList<>();
}
