package com.mammon.goods.domain.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class SpuDto {
    /**
     * 商品分类
     */
    @NotNull(message = "商品分类不能为空")
    private String categoryId;

    /**
     * 编码
     */
    private String spuCode;

    /**
     * 条码
     */
    private String spuNo;

    /**
     * 一品多码(串码)
     */
    private String manyCode;

    /**
     * 商品名
     */
    @NotBlank(message = "商品名不能为空")
    private String name;

    /**
     * 单位
     */
    @NotBlank(message = "单位不能为空")
    private String unitId;

    /**
     * 是否可计次核销（0:不计次，1:可计次）
     */
    private int countedType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 库存(>0)同步到此门店
     */
    @Min(value = 1, message = "库存同步到门店不能为空")
    private long syncStoreNo;

    /**
     * 商品图片
     */
    private List<String> pictures = new ArrayList<>();

    @Valid
    @NotNull(message = "商品信息不能为空")
    private List<SkuDto> skus = new ArrayList<>();
}
