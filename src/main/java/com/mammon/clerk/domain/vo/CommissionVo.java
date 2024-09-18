package com.mammon.clerk.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/4/8 10:14
 */
@Data
public class CommissionVo {

    private long merchantNo;

    private long storeNo;

    /**
     * 店员id
     */
    private String accountId;

    private String accountName;

    /**
     * 提成总额
     */
    private BigDecimal totalAmount;

    /**
     * 销售提成
     */
    private BigDecimal cashierAmount;

    /**
     * 储值提成
     */
    private BigDecimal rechargeAmount;

    /**
     * 计次提成
     */
    private BigDecimal countedAmount;

    /**
     * 服务提成
     */
    private BigDecimal serviceAmount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
