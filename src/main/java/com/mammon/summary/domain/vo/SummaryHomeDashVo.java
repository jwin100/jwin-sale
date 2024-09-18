package com.mammon.summary.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @since 2024/2/29 14:32
 */
@Data
public class SummaryHomeDashVo {
    /**
     * 销售单数
     */
    private long cashierTotal;

    /**
     * 销售金额
     */
    private BigDecimal cashierAmount;

    /**
     * 会员注册数
     */
    private long memberRegisterTotal;

    /**
     * 储值金额
     */
    private BigDecimal memberRechargeAmount;
}
