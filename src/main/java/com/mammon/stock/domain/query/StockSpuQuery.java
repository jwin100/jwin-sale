package com.mammon.stock.domain.query;

import lombok.Data;

import java.util.List;

/**
 * @author dcl
 * @since 2024/3/14 16:49
 */
@Data
public class StockSpuQuery {

    private Long storeNo;

    private List<String> spuIds;

    private List<String> categoryIds;

    /**
     * 商品关键词搜索
     */
    private String searchKey;

    /**
     * 是否服务商品
     */
    private Integer countedType;

    private Integer status;
}
