package com.mammon.summary.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author dcl
 * @since 2024/5/31 14:46
 */
@Data
public class SummaryStoreVo {

    /**
     * 统计日期
     */
    private LocalDate summaryDate;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 销售金额
     */
    private BigDecimal cashierAmount;

    /**
     * 储值金额
     */
    private BigDecimal rechargeAmount;

    /**
     * 开卡金额
     */
    private BigDecimal countedAmount;

    /**
     * 服务金额
     */
    private BigDecimal serviceAmount;
}
