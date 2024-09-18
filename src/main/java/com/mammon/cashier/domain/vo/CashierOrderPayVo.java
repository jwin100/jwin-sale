package com.mammon.cashier.domain.vo;

import com.mammon.cashier.domain.model.CashierTimeCardModel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CashierOrderPayVo {

    private String id;

    private String orderId;

    /**
     * 支付方式
     */
    private int payCode;

    private String payCodeName;

    /**
     * 应收
     */
    private BigDecimal payableAmount;

    /**
     * 实收
     */
    private BigDecimal realityAmount;

    /**
     * 第三方交易流水号
     */
    private String tradeNo;

    private LocalDateTime tradeTime;

    /**
     * 交易状态
     */
    private int status;

    private String statusName;

    private String message;

    private LocalDateTime createTime;
}
