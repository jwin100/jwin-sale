package com.mammon.office.order.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @since 2023/8/7 11:49
 */
@Data
public class TradePreCreateDto {

    private String orderNo;

    private String subject;

    private BigDecimal payableAmount;
}
