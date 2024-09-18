package com.mammon.cashier.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CashierOrderPayDto {
    @NotBlank(message = "订单错误")
    private String id;

    /**
     * 支付方式
     */
    private int payCode;
    /**
     * 应收
     */
    private BigDecimal payAmount;

    private String authCode;

    private String payPwd;

    private String countedId;

    private long countedTotal;
}
