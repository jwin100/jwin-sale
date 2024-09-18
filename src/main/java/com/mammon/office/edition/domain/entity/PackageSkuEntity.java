package com.mammon.office.edition.domain.entity;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-02-02 11:27:22
 */
@Data
public class PackageSkuEntity {

    private String id;

    private String spuId;

    private String industryId;

    private String name;

    /**
     * 开通数量
     */
    private long quantity;

    private String quantityDesc;

    private int type;

    private int unit;

    /**
     * 价格
     */
    private long nowAmount;

    /**
     * 原价
     */
    private long originalAmount;

    private int sort;

    private String unitPriceDesc;

    private String specs;
}
