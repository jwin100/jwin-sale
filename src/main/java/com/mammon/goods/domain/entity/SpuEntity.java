package com.mammon.goods.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * spu
 */
@Data
public class SpuEntity {

    private String id;

    private long merchantNo;

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
     * 备注
     */
    private String remark;

    /**
     * 状态(上架，下架)
     */
    private int status;

    /**
     * 是否删除（1:删除，0:未删除）
     */
    private int deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}