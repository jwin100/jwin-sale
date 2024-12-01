package com.mammon.goods.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.goods.domain.enums.SpuCountedType;
import com.mammon.goods.domain.enums.SpuStatus;
import com.mammon.goods.domain.enums.UnitType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SkuDetailVo {

    private String spuId;

    private String skuId;

    private long merchantNo;

    /**
     * 商品分类
     */
    private String categoryId;

    private List<String> categoryIds = new ArrayList<>();

    private String categoryName;

    private String lastCategoryName;

    /**
     * 编码
     */
    private String spuCode;

    /**
     * 条码
     */
    private String spuNo;

    /**
     * 一品多码
     */
    private String manyCode;

    /**
     * 商品名
     */
    private String name;

    /**
     * 单位
     */
    private String unitId;

    private String unitName;

    private int unitType;

    private String unitTypeName;

    /**
     * 是否可计次商品
     */
    private int countedType;

    private String countedTypeName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态(上架，下架)
     */
    private int status;

    private String statusName;

    private LocalDateTime createTime;

    /**
     * 商品图片
     */
    private List<String> pictures = new ArrayList<>();

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
     * 零售价(分)
     */
    private BigDecimal referenceAmount;

    /**
     * 重量
     */
    private BigDecimal skuWeight;

    /**
     * 规格
     */
    private List<SkuSpecVo> specs = new ArrayList<>();

    private List<SkuTagVo> tags = new ArrayList<>();

    public String getCountedTypeName() {
        return IEnum.getNameByCode(this.getCountedType(), SpuCountedType.class);
    }

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), SpuStatus.class);
    }

    public String getUnitTypeName() {
        return IEnum.getNameByCode(this.getUnitType(), UnitType.class);
    }
}
