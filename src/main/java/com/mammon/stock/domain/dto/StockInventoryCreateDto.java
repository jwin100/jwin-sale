package com.mammon.stock.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author dcl
 * @since 2024/4/1 17:39
 */
@Data
public class StockInventoryCreateDto {

    /**
     * 盘点范围
     */
    private int range;

    /**
     * 指定分类
     */
    private List<String> categories;

    private String remark;
}
