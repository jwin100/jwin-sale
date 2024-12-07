package com.mammon.goods.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SkuPageQuery extends PageQuery {
    /**
     * 商品编码、商品条码、商品名
     */
    private String searchKey;

    /**
     * 商品状态(1:启用，2：禁用)
     */
    private Integer status;

    private String categoryId;

    private List<String> categoryIds;

    private Integer countedType;
}
