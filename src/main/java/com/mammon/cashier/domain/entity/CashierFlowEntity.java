package com.mammon.cashier.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 自定义流水
 */
@Data
public class CashierFlowEntity {

    private String id;

    private long merchantNo;

    /**
     * 牌号模式(0:自增，1:手动
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

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
