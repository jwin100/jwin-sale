package com.mammon.stock.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门店spu信息
 *
 * @author dcl
 * @since 2024/6/27 13:44
 */
@Data
public class StockSpuEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

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
     * 是否服务商品
     */
    private int countedType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态(上架，下架)
     */
    private int status;

    /**
     * 商品库状态（启用，禁用）
     */
    private int goodsStatus;

    /**
     * 是否删除（1:删除，0:未删除）
     */
    private int deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
