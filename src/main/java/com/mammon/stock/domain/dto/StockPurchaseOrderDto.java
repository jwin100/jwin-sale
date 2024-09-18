package com.mammon.stock.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class StockPurchaseOrderDto {
    /**
     * 采购门店
     */
    private long storeNo;

    /**
     * 送达日期
     */
    private LocalDateTime deliveryTime;

    private String remark;

    private List<StockPurchaseOrderSkuDto> skus = new ArrayList<>();
}
