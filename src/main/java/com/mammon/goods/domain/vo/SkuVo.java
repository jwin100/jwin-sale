package com.mammon.goods.domain.vo;

import com.mammon.goods.domain.dto.SkuSpecDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class SkuVo {

    private String id;

    private String spuId;

    /**
     * 规格编码
     */
    private String skuCode;

    /**
     * 规格条码
     */
    private String skuNo;

    private String skuName;

    private String picture;

    /**
     * 进价
     */
    private BigDecimal purchaseAmount;

    /**
     * 零售价(分)
     */
    private BigDecimal referenceAmount;

    /**
     * 总库存
     */
    private BigDecimal sellStock;

    /**
     * 重量
     */
    private BigDecimal skuWeight;

    /**
     * 商品子状态
     */
    private int status;

    /**
     * 规格
     */
    private List<SkuSpecVo> specs = new ArrayList<>();

    private List<SkuTagVo> tags = new ArrayList<>();
}
