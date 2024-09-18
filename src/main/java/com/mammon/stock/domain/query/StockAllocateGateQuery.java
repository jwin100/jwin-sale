package com.mammon.stock.domain.query;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author dcl
 * @since 2023/12/27 15:51
 */
@Data
public class StockAllocateGateQuery {

    /**
     * 调入方
     */
    @NotNull(message = "调入方门店不能为空")
    private Long inStoreNo;

    /**
     * 调出方
     */
    @NotNull(message = "调出方门店不能为空")
    private Long outStoreNo;
}
