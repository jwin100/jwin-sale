package com.mammon.office.edition.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.office.edition.domain.enums.IndustryType;
import lombok.Data;

import java.util.List;

/**
 * @author dcl
 * @date 2023-02-15 19:23:05
 */
@Data
public class PackageSpuListVo {

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

    private String typeName;

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
    private List<String> abilityDesc;

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

    public String getTypeName() {
        return IEnum.getNameByCode(this.getType(), IndustryType.class);
    }
}
