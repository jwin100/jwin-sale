package com.mammon.goods.domain.vo;

import lombok.Data;

@Data
public class SkuTagVo {

    private String id;

    private String skuId;

    private String tagId;

    private String tagName;

    private int status;
}
