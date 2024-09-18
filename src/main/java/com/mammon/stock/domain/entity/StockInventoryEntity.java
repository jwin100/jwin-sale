package com.mammon.stock.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门店盘点
 *
 * @author dcl
 * @since 2024/4/1 15:59
 */
@Data
public class StockInventoryEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    /**
     * 盘点单号
     */
    private String inventoryNo;

    /**
     * 盘点范围
     */
    private int range;

    /**
     * 指定分类
     */
    private String categories;

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

    /**
     * 盘点备注
     */
    private String remark;

    /**
     * 1：创建，2：盘点中，3：结束
     */
    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
