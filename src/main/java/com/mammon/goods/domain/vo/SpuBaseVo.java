package com.mammon.goods.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.goods.domain.enums.SpuStatus;
import com.mammon.goods.domain.enums.UnitType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SpuBaseVo {

    private String id;

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

    private int countedType;

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

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), SpuStatus.class);
    }

    public String getUnitTypeName() {
        return IEnum.getNameByCode(this.getUnitType(), UnitType.class);
    }
}
