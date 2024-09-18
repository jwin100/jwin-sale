package com.mammon.summary.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author dcl
 * @since 2024/5/31 10:50
 */
@Data
public class SummaryAccountVo {

    private long merchantNo;

    private String merchantName;

    private long storeNo;

    private String storeName;

    /**
     * 店员id
     */
    private String accountId;

    /**
     * 店员名
     */
    private String accountName;

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