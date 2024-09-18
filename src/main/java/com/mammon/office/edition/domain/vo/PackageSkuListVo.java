package com.mammon.office.edition.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.office.edition.domain.enums.IndustryType;
import com.mammon.office.edition.domain.enums.IndustryUnit;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @date 2023-03-06 11:37:55
 */
@Data
public class PackageSkuListVo {

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

    private String typeName;

    private int unit;

    private String unitName;

    /**
     * 价格
     */
    private BigDecimal nowAmount;

    private BigDecimal originalAmount;

    private int sort;

    private String unitPriceDesc;

    private String specs;

    public String getUnitName() {
        return IEnum.getNameByCode(this.getUnit(), IndustryUnit.class);
    }

    public String getTypeName() {
        return IEnum.getNameByCode(this.getType(), IndustryType.class);
    }
}
