package com.mammon.print.domain.dto;

import lombok.Data;

@Data
public class PrintActiveProductItemDto {

    /**
     * 商品名
     */
    private String name;

    /**
     * 单价
     */
    private String referenceAmount;

    /**
     * 商品数量
     */
    private String quantity;

    /**
     * 商品折扣
     */
    private String discount;

    /**
     * 商品小计
     */
    private String realAmount;
}
