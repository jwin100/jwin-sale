package com.mammon.stock.domain.dto;

import lombok.Data;

@Data
public class SellStockChangeDto {

    private String skuId;

    private long quantity;

    private int type;

    private int wayType;

    private String remark;
}
