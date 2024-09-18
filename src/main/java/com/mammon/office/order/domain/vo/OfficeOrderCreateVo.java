package com.mammon.office.order.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dcl
 * @date 2023-02-02 17:02:20
 */
@Data
public class OfficeOrderCreateVo {

    private String orderId;

    private String orderNo;

    private String subject;

    private BigDecimal payableAmount;
}
