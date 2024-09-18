package com.mammon.stock.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @since 2024/3/12 15:18
 */
@Data
public class StockClaimDto {

    /**
     * 大件商品
     */
    private String largeSpuId;

    private String largeSpuNo;

    private String largeSpuCode;

    private String largeSpuName;

    private String largeUnitId;

    /**
     * 小件商品
     */
    private String smallSpuId;

    private String smallSpuNo;

    private String smallSpuCode;

    private String smallSpuName;

    private String smallUnitId;

    /**
     * 大包装数量 =小包装数量x倍数
     */
    private long multiple;

    /**
     * 组装拆包规格对应关系
     */
    private List<StockClaimSkuDto> skus = new ArrayList<>();
}
