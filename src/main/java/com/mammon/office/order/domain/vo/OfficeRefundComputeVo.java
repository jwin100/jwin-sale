package com.mammon.office.order.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author dcl
 * @date 2023-03-14 09:59:20
 */
@Data
public class OfficeRefundComputeVo {

    private String orderId;

    private String orderNo;

    /**
     * 退款总金额
     */
    private BigDecimal refundAmount;

    private List<OfficeRefundComputeItemVo> items;
}
