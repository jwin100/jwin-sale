package com.mammon.stock.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @date 2023-06-08 15:40:11
 */
@Data
public class StockStoreLockDto {

    private String spuId;

    private String skuId;

    private long merchantNo;

    private long storeNo;

    private String orderId;

    private BigDecimal lockStock;
}
