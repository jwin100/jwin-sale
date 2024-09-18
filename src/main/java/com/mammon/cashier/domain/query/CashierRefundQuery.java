package com.mammon.cashier.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class CashierRefundQuery extends PageQuery {

    /**
     * 订单号
     */
    private String orderNo;

    private String refundNo;

    /**
     * 商品名 || 条码 || spuId
     */
    private String searchKey;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 会员id
     */
    private String memberId;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 退回方式
     */
    private Integer refundMode;

    /**
     * 销售人员
     */
    private String operationId;

    private LocalDate startDate;

    private LocalDate endDate;
}
