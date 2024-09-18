package com.mammon.goods.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-03-22 14:31:54
 */
@Data
public class SkuSpecEntity {

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

    private int status;

    private int deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
