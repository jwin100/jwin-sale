package com.mammon.cashier.domain.entity;

import com.mammon.cashier.domain.enums.CashierOrderType;
import com.mammon.cashier.domain.enums.CashierRefundMark;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 销售订单
 */
@Data
public class CashierOrderEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String customerNo;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 1：商品销售，2：余额储值，3：计次开卡
     */
    private int category;

    /**
     * @see CashierOrderType
     */
    private int type;

    private String subject;

    /**
     * 原价
     */
    private long originalAmount;

    /**
     * 抹零类型
     */
    private int ignoreType;

    /**
     * 抹零金额
     */
    private long ignoreAmount;

    /**
     * 整单折扣
     */
    private long discount;

    /**
     * 折扣金额
     */
    private long discountAmount;

    /**
     * 优惠金额
     */
    private long preferentialAmount;

    /**
     * 优惠后金额
     */
    private long collectAmount;

    /**
     * 调整金额
     */
    private long adjustAmount;

    /**
     * 应收=原价-优惠金额+调整金额
     */
    private long payableAmount;

    /**
     * 实付
     */
    private long realityAmount;

    /**
     * 核销次数
     */
    private long countedTotal;

    /**
     * 订单积分
     */
    private long integral;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 订单状态
     */
    private int status;

    private String message;

    /**
     * 退款标识
     *
     * @see CashierRefundMark
     */
    private int refundMark;

    /**
     * 退货总金额
     */
    private long refundAmount;

    /**
     * 已退总积分
     */
    private long refundIntegral;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 发送短信,1:发送,0:不发
     */
    private int sendSms;

    /**
     * 操作人(下单人)
     */
    private String operationId;

    /**
     * 服务人员(json字符串)
     */
    private String serviceAccountIds;

    /**
     * 订单来源
     *
     * @see com.mammon.enums.CommonSource
     */
    private int source;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 销售时间
     */
    private LocalDateTime cashierTime;

    /**
     * 订单创建日期
     */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private int deleted;
}
