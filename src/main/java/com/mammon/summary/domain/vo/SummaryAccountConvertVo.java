package com.mammon.summary.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @since 2024/6/5 10:19
 */
@Data
public class SummaryAccountConvertVo {

    private String accountId;

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
}
