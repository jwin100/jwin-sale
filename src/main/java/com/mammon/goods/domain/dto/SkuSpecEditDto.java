package com.mammon.goods.domain.dto;

import lombok.Data;

@Data
public class SkuSpecEditDto {
    
    /**
     * 规格键(specId)
     */
    private String specKeyId;

    /**
     * 规格值(specId)
     */
    private String specValueId;

    private int status;
}
