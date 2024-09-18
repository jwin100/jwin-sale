package com.mammon.goods.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StorageSpuImportDto {

    @ExcelProperty("商品名(必填)")
    private String name;

    @ExcelProperty("商品编码")
    private String spuCode;

    @ExcelProperty("商品条码")
    private String spuNo;

    @ExcelProperty("一品多码(串码)")
    private String manyBarCode;

    @ExcelProperty("一级分类")
    private String categoryOne;

    @ExcelProperty("二级分类")
    private String categoryTwo;

    @ExcelProperty("库存单位")
    private String unitName;

    @ExcelProperty("单位计价方式")
    private String unitType;

    @ExcelProperty("商品图片")
    private String pictures;

    @ExcelProperty("规格项1")
    private String specKeyOne;

    @ExcelProperty("规格值1")
    private String specValueOne;

    @ExcelProperty("规格项2")
    private String specKeyTwo;

    @ExcelProperty("规格值2")
    private String specValueTwo;

    @ExcelProperty("规格项3")
    private String specKeyThree;

    @ExcelProperty("规格值3")
    private String specValueThree;

    @ExcelProperty("规格项4")
    private String specKeyFour;

    @ExcelProperty("规格值4")
    private String specValueFour;

    @ExcelProperty("规格型号")
    private String specName;

    @ExcelProperty("规格编码")
    private String skuCode;

    @ExcelProperty("规格条码")
    private String skuNo;

    @ExcelProperty("进价(元)")
    private BigDecimal purchaseAmount;

    @ExcelProperty("零售价(元)")
    private BigDecimal referenceAmount;

    @ExcelProperty("重量(千克)")
    private BigDecimal skuWeight;

    @ExcelProperty("初始库存")
    private BigDecimal sellStock;
}
