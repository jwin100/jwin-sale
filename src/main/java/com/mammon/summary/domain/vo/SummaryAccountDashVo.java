package com.mammon.summary.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @since 2024/5/31 10:48
 */
@Data
public class SummaryAccountDashVo {

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
