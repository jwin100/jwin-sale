package com.mammon.cashier.domain.vo;

import com.mammon.cashier.domain.enums.CashierRefundStatus;
import com.mammon.enums.IEnum;
import com.mammon.member.domain.vo.MemberInfoVo;
import com.mammon.clerk.domain.vo.UserVo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CashierRefundDetailVo {
    private String id;

    private long merchantNo;

    private long storeNo;

    private String storeName;

    private String refundNo;

    private String orderId;

    private String orderNo;

    private String subject;

    /**
     * 原价
     */
    private BigDecimal originalAmount;

    /**
     * 手动调整
     */
    private BigDecimal adjustAmount;

    /**
     * 应退
     */
    private BigDecimal payableAmount;

    /**
     * 实退
     */
    private BigDecimal realityAmount;

    /**
     * 退积分
     */
    private long integral;

    /**
     * 退计次
     */
    private long countedTotal;

    /**
     * 订单状态
     */
    private int status;

    private String statusName;

    /**
     * 订单状态描述
     */
    private String message;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 操作人(下单人)
     */
    private String operationId;


    /**
     * 退款方式,0:原路退回，1:自行选择
     */
    private int refundMode;

    /**
     * 退货原因
     */
    private String refundCause;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 订单创建日期
     */
    private LocalDateTime createTime;

    /**
     * 操作人信息
     */
    private UserVo operationAccount;


    /**
     * 会员信息
     */
    private MemberInfoVo member;

    private List<CashierRefundProductVo> skus = new ArrayList<>();

    private List<CashierRefundPayVo> pays = new ArrayList<>();

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), CashierRefundStatus.class);
    }
}
