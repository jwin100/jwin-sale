package com.mammon.stock.domain.vo;

import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @date 2023-03-27 13:26:36
 */
@Data
public class StockSpuVo {

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
     * 商品spuId
     */
    private String spuId;

    /**
     * 商品分类
     */
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

    /**
     * 是否服务商品
     */
    private int countedType;

    /**
     * 规格数
     */
    private long specs;

    /**
     * 总库存
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

    /**
     * 商品库商品状态
     */
    private int goodsStatus;

    private String goodsStatusName;

    private LocalDateTime createTime;

    /**
     * 商品图片
     */
    private List<String> pictures = new ArrayList<>();

    private List<StockSkuDetailListVo> skus = new ArrayList<>();

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), CommonStatus.class);
    }

    public String getGoodsStatusName() {
        return IEnum.getNameByCode(this.getGoodsStatus(), CommonStatus.class);
    }
}
