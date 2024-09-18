package com.mammon.goods.domain.dto;

import lombok.Data;

@Data
public class SpecDto {
    /**
     * 规格名
     */
    private String name;

    /**
     * null为规格名，不为null为对应规格名的值
     */
    private String pid;
}
