package com.mammon.goods.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SpuPageQuery extends PageQuery {

    /**
     * 条码
     */
    private String spuNo;

    /**
     * 条码
     */
    private String spuCode;

    /**
     * 商品名
     */
    private String name;

    /**
     * 商品条码，商品编码，商品名，规格条码，规格编码，规格名
     */
    private String searchKey;

    /**
     * 商品分类
     */
    private String categoryId;

    private List<String> categoryIds = new ArrayList<>();

    private Integer countedType;

    private String unitId;

    /**
     * 状态(上架，下架)
     */
    private Integer status;
}
