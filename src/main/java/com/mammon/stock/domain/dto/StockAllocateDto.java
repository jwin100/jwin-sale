package com.mammon.stock.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StockAllocateDto {
    /**
     * 调入方
     */
    private long inStoreNo;

    /**
     * 调出方
     */
    private long outStoreNo;

    private String remark;

    private List<StockAllocateSkuDto> skus = new ArrayList<>();
}
