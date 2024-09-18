package com.mammon.member.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员消费统计
 *
 * @author dcl
 * @since 2024/7/31 15:29
 */
@Data
public class MemberSummaryCashierVo {

    /**
     * 会员id
     */
    private String id;

    /**
     * 订单总金额
     */
    private BigDecimal cashierAmount;

    /**
     * 订单笔数
     */
    private long cashierTotal;

    /**
     * 客单价
     */
    private BigDecimal referenceAmount;

    /**
     * 最近消费日期
     */
    private LocalDateTime lastCashierTime;

    /**
     * 退款总金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款笔数
     */
    private long refundTotal;
}
