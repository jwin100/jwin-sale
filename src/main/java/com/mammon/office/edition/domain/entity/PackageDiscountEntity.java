package com.mammon.office.edition.domain.entity;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-02-02 11:29:11
 * 折扣表
 */
@Data
public class PackageDiscountEntity {

    private String id;

    private String packageId;

    /**
     * 折扣类型(1:数量折扣（购买几个打几折），2：数量满减(购买几个减金额)，3：金额满减（满多少金额减金额），4:买送（买几个送多少）)
     */
    private int type;

    /**
     * 条件
     */
    private double condition;

    /**
     * 折扣
     */
    private double discount;
}
