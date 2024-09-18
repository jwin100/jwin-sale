package com.mammon.goods.domain.vo;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-03-22 17:29:24
 */
@Data
public class SkuSpecVo {

    private String id;

    private String spuId;

    private String skuId;

    /**
     * 规格键id
     */
    private String specId;

    /**
     * 规格键名称
     */
    private String specName;

    /**
     * 规格值id
     */
    private String specValueId;

    /**
     * 规格值名称
     */
    private String specValueName;
}
