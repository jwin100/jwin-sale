package com.mammon.stock.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @date 2023-03-27 13:19:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StockSpuPageQuery extends PageQuery {

    private Long storeNo;

    private List<String> spuIds;

    /**
     * 条码
     */
    private String spuNo;

    /**
     * 编码
     */
    private String spuCode;

    /**
     * 商品名
     */
    private String name;

    /**
     * 商品分类
     */
    private String categoryId;

    private List<String> categoryIds = new ArrayList<>();

    private String searchKey;

    /**
     * 计次核销
     */
    private Integer countedType;

    /**
     * 状态(上架，下架)
     */
    private Integer status;

    private String unitId;
}
