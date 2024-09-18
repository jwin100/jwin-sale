package com.mammon.goods.domain.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuDto {

    private String id;

    /**
     * 规格编码
     */
    private String skuCode;

    /**
     * 规格条码
     */
    private String skuNo;

    private String skuName;

    /**
     * 进价
     */
    private BigDecimal purchaseAmount;

    /**
     * 零售价
     */
    @NotNull(message = "商品零售价不能为空")
    private BigDecimal referenceAmount;

    /**
     * 库存
     */
    private BigDecimal sellStock;

    /**
     * 重量
     */
    private BigDecimal skuWeight;

    private List<SkuSpecDto> specs;

    private List<SkuTagEditDto> tags;
}
