package com.mammon.goods.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.goods.domain.enums.UnitType;
import lombok.Data;

@Data
public class UnitVo {

    private String id;

    private long merchantNo;

    private String name;

    /**
     * 单位计价方式
     */
    private int type;

    private String typeDesc;

    private int status;

    public String getTypeDesc() {
        return IEnum.getNameByCode(this.getType(), UnitType.class);
    }
}
