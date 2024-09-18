package com.mammon.office.edition.domain.entity;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-02-02 11:23:51
 * <p>
 * 套餐包
 */
@Data
public class PackageSpuEntity {

    private String id;

    /**
     * 套餐名称
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

    /**
     * 套餐包分类(1:版本，2：短信，3：门店额度，。。。其他组合型套餐)
     */
    private int type;

    /**
     * 显示顺序
     */
    private int sort;

    /**
     * 单价描述
     */
    private String unitPriceDesc;

    /**
     * 功能描述(json字符串)
     */
    private String abilityDesc;

    /**
     * 备注
     */
    private String remark;

    /**
     * 1：上架，2：下架
     */
    private int status;

    /**
     * 是否检查库存(1:不检查，2:检查)
     */
    private int checkStock;

    /**
     * 库存数
     */
    private Long stock;
}
