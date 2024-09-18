package com.mammon.cashier.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CashierHangItemVo {

    private String key;

    private String skuId;

    private String skuName;

    private String specName;

    private List<String> pictures = new ArrayList<>();

    /**
     * 数量
     */
    private long quantity;

    /**
     * 单价
     */
    private BigDecimal referenceAmount;

    /**
     * 调整金额
     */
    private BigDecimal adjustAmount;

    /**
     * 小计
     */
    private BigDecimal payableAmount;
}
