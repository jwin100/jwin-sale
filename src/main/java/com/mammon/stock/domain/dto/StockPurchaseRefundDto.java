package com.mammon.stock.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StockPurchaseRefundDto {

    private String purchaseId;

    /**
     * 退货原因
     */
    private String reasonId;

    private String remark;

    private List<StockPurchaseRefundSkuDto> skus = new ArrayList<>();
}
