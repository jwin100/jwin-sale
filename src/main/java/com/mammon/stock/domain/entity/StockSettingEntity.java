package com.mammon.stock.domain.entity;

import lombok.Data;

/**
 * @author dcl
 * @since 2023/12/27 10:17
 */
@Data
public class StockSettingEntity {

    private long merchantNo;

    /**
     * 采购订单是否启用审核
     */
    private int purchaseOrderExamine;

    private int purchaseRefundExamine;

    private int allocateExamine;

    /**
     * 库存为0时是否自动组装拆包
     */
    private int claim;
}
