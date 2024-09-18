package com.mammon.stock.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.stock.domain.enums.StockInventoryRange;
import com.mammon.stock.domain.enums.StockInventoryStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @since 2024/4/1 17:20
 */
@Data
public class StockInventoryDetailVo {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String storeName;

    /**
     * 盘点单号
     */
    private String inventoryNo;

    /**
     * 盘点范围
     */
    private int range;

    private String rangeName;

    /**
     * 指定分类
     */
    private List<String> categories = new ArrayList<>();

    /**
     * 盘点开始时间
     */
    private LocalDateTime inventoryStartTime;

    /**
     * 盘点结束时间
     */
    private LocalDateTime inventoryEndTime;

    /**
     * 盘点商品（种）
     */
    private long productNum;

    /**
     * 盘点异常商品（种）
     */
    private long errorProductNum;

    /**
     * 盘点人
     */
    private String operationId;

    private String operationName;

    /**
     * 盘点备注
     */
    private String remark;

    /**
     * 1：创建，2：盘点中，3：结束
     */
    private int status;

    private String statusName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<StockInventoryProductVo> products = new ArrayList<>();

    public String getRangeName() {
        return IEnum.getNameByCode(this.getRange(), StockInventoryRange.class);
    }

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), StockInventoryStatus.class);
    }
}
