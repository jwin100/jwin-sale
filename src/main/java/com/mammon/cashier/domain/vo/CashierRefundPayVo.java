package com.mammon.cashier.domain.vo;

import com.mammon.cashier.domain.model.CashierTimeCardModel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CashierRefundPayVo {

    private String id;

    private String refundId;

    /**
     * 支付方式
     */
    private int payCode;

    private String payCodeName;
    /**
     * 应退
     */
    private BigDecimal payableAmount;

    /**
     * 实退
     */
    private BigDecimal realityAmount;

    private String countedId;

    private long countedTotal;

    /**
     * 第三方交易流水号
     */
    private String tradeNo;

    private LocalDateTime tradeTime;

    /**
     * 交易状态
     */
    private int status;

    private String message;

    private LocalDateTime createTime;
}
