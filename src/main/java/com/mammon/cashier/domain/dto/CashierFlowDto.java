package com.mammon.cashier.domain.dto;

import lombok.Data;

@Data
public class CashierFlowDto {
    /**
     * 牌号模式(自增，手动
     */
    private int customFlowModel;

    /**
     * 流水重置频率(自增模式下 (0:不重置，1：每天，2：每月，3：每季度，
     */
    private int ratingRestFlow;

    /**
     * 初始牌号(自增模式下
     */
    private long initFlow;
}
