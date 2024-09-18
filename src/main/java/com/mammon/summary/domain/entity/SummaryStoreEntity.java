package com.mammon.summary.domain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 门店业绩统计
 *
 * @author dcl
 * @since 2024/4/16 17:54
 */
@Data
public class SummaryStoreEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    /**
     * 统计日期
     */
    private LocalDate summaryDate;

    /**
     * 总金额
     */
    private long totalAmount;

    /**
     * 销售金额
     */
    private long cashierAmount;

    /**
     * 储值金额
     */
    private long rechargeAmount;

    /**
     * 开卡金额
     */
    private long countedAmount;

    /**
     * 服务金额
     */
    private long serviceAmount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
