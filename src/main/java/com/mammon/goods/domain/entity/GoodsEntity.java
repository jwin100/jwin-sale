package com.mammon.goods.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GoodsEntity {

    private String id;

    /**
     * 条码
     */
    private String barcode;

    /**
     * 商品名
     */
    private String name;

    /**
     * 分类码
     */
    private String classCode;

    /**
     * 商标
     */
    private String brandName;

    /**
     * 规格
     */
    private String specification;

    private long price;

    /**
     * 宽度
     */
    private String width;

    /**
     * 高度
     */
    private String height;

    /**
     * 深度
     */
    private String depth;

    /**
     * 原产国
     */
    private String codeSource;

    /**
     * 发布厂家
     */
    private String firmName;

    /**
     * 净重
     */
    private String grossWeight;

    /**
     * 批次
     */
    private String batch;

    /**
     * 公司地址
     */
    private String firmAddress;

    /**
     * 公司状态
     */
    private String firmStatus;

    /**
     * 网站链接
     */
    private String firmDescription;

    /**
     * 条码状态
     */
    private String barcodeStatus;

    /**
     * 图片链接(json)
     */
    private String image;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
