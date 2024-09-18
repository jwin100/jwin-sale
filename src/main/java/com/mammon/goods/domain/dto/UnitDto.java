package com.mammon.goods.domain.dto;

import lombok.Data;

@Data
public class UnitDto {

    private String name;

    /**
     * 单位计价方式
     */
    private int type;
}
