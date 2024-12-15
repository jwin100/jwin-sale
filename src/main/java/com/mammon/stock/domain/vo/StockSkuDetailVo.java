package com.mammon.stock.domain.vo;

import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import com.mammon.goods.domain.enums.UnitType;
import com.mammon.goods.domain.vo.SkuSpecVo;
import com.mammon.goods.domain.vo.SkuTagVo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @date 2023-03-31 14:11:19
 */
@Data
public class StockSkuDetailVo {

    private String skuId;

    /**
     * 商品spuId
     */
    private String spuId;

    private long merchantNo;

    /**
     * 门店号
     */
    private long storeNo;

    /**
     * 门店名
     */
    private String storeName;


    /**
     * 商品分类
     */
    private String categoryId;

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

    /**
     * 单位类型
     */
    private Integer unitType;

    private String unitTypeName;

    /**
     * 是否服务商品
     */
    private int countedType;

    /**
     * 库存
     */
    private BigDecimal sellStock;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态(上架，下架)
     */
    private int status;

    private String statusName;

    private int goodsStatus;

    private String goodsStatusName;

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
     * 零售价
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

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), CommonStatus.class);
    }

    public String getUnitTypeName() {
        return IEnum.getNameByCode(this.getUnitType(), UnitType.class);
    }

    public String getGoodsStatusName() {
        return IEnum.getNameByCode(this.getGoodsStatus(), CommonStatus.class);
    }
}
