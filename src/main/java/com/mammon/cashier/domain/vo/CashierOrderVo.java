package com.mammon.cashier.domain.vo;

import com.mammon.cashier.domain.enums.CashierOrderStatus;
import com.mammon.cashier.domain.enums.CashierOrderType;
import com.mammon.cashier.domain.enums.CashierRefundMark;
import com.mammon.enums.CommonSource;
import com.mammon.enums.IEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CashierOrderVo {

    private String id;

    private long merchantNo;

    private long storeNo;

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
    private BigDecimal originalAmount;

    /**
     * 抹零类型
     */
    private int ignoreType;

    private String ignoreTypeName;

    /**
     * 抹零金额
     */
    private BigDecimal ignoreAmount;

    /**
     * 整单折扣
     */
    private long discount;


    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 优惠金额
     */
    private BigDecimal preferentialAmount;

    /**
     * 优惠后金额
     */
    private BigDecimal collectAmount;

    /**
     * 调整金额(手动加减价)
     */
    private BigDecimal adjustAmount;

    /**
     * 应收=原价-优惠金额+调整金额
     */
    private BigDecimal payableAmount;

    /**
     * 实收
     */
    private BigDecimal realityAmount;

    /**
     * 本单积分
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

    private String statusName;

    private String message;

    /**
     * 退款标识
     *
     * @see CashierRefundMark
     */
    private int refundMark;

    private String refundMarkName;

    /**
     * 退货总金额
     */
    private BigDecimal refundAmount;

    /**
     * 已退积分
     */
    private long refundIntegral;

    /**
     * 会员
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
     * 服务人员
     */
    private List<String> serviceAccountIds;

    /**
     * 订单来源
     */
    private int source;

    private String sourceName;

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

    public String getSourceName() {
        return IEnum.getNameByCode(this.getSource(), CommonSource.class);
    }

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), CashierOrderStatus.class);
    }

    public String getRefundMarkName() {
        return IEnum.getNameByCode(this.getRefundMark(), CashierRefundMark.class);
    }

    public String getIgnoreTypeName() {
        return null;
    }
}
