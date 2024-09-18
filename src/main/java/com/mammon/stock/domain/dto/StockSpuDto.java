package com.mammon.stock.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 门店spu信息
 *
 * @author dcl
 * @since 2024/6/27 13:44
 */
@Data
public class StockSpuDto {

    private String spuId;

    /**
     * 商品分类
     */
    private String categoryId;

    /**
     * 编码
     */
    private String spuCode;

    /**
     * 条码
     */
    private String spuNo;

    /**
     * 一品多码
     */
    private String manyCode;

    /**
     * 商品名
     */
    private String name;

    /**
     * 单位
     */
    private String unitId;

    /**
     * 商品图片
     */
    private String pictures;

    /**
     * 是否可计次核销
     */
    private int countedType;

    /**
     * 商品状态
     */
    private int status;

    /**
     * 备注
     */
    private String remark;

    private long syncStoreNo;

    /**
     * 商品规格信息
     */
    private List<StockSkuDto> skus;
}
