package com.mammon.stock.domain.vo;

import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import com.mammon.goods.domain.enums.UnitType;
import com.mammon.goods.domain.vo.SkuSpecVo;
import com.mammon.goods.domain.vo.SkuTagVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @date 2023-03-31 14:11:19
 */
@Data
public class StockSkuVo {
    private String spuId;

    private String skuId;

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

    private String statusName;

    private String unitId;

    private String unitName;

    private Integer unitType;

    private String unitTypeName;

    /**
     * 是否服务商品
     */
    private int countedType;

    private String joinSpec;

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
}
