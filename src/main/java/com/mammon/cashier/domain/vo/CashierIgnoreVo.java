package com.mammon.cashier.domain.vo;

import com.mammon.cashier.domain.enums.CashierIgnoreType;
import com.mammon.enums.IEnum;
import lombok.Data;

/**
 * @author dcl
 * @since 2023/11/9 16:01
 */
@Data
public class CashierIgnoreVo {

    private String id;

    private long merchantNo;

    /**
     * 抹零类型，0:不抹零，1:抹分，2:抹角
     */
    private int type;

    private String typeName;

    public String getTypeName() {
        return IEnum.getNameByCode(this.getType(), CashierIgnoreType.class);
    }
}
