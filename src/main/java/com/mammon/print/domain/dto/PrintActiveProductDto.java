package com.mammon.print.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品基础信息
 */
@Data
public class PrintActiveProductDto {

    private List<PrintActiveProductItemDto> items = new ArrayList<>();

    /**
     * 合计数量
     */
    private String productTotal;

    /**
     * 合计
     */
    private String realAmount;
}
