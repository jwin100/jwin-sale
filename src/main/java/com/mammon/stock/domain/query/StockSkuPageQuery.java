package com.mammon.stock.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author dcl
 * @since 2024/7/10 20:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StockSkuPageQuery extends PageQuery {

    /**
     * 商品编码、商品条码、商品名
     */
    private String searchKey;

    private Long storeNo;

    /**
     * 商品状态(1:启用，2：禁用)
     */
    private Integer status;

    private String categoryId;

    private List<String> categoryIds;

    private Integer countedType;
}
