package com.mammon.cashier.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class CashierOrderPageQuery extends PageQuery {

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 商品名 || 条码 || spuId
     */
    private String searchKey;

    /**
     * 1：商品销售，2：余额储值，3：计次开卡
     */
    private Integer category;

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
    private Integer status;

    /**
     * 销售人员
     */
    private String operationId;

    private LocalDate startDate;

    private LocalDate endDate;
}