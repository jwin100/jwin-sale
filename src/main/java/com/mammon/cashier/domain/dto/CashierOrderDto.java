package com.mammon.cashier.domain.dto;

import com.mammon.cashier.domain.enums.CashierOrderType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CashierOrderDto {

    /**
     * 方式重复提交
     */
    private long requestId;

    /**
     * 自定义单号
     */
    private String customerNo;

    /**
     * 1：商品销售，2：余额储值，3：计次开卡
     */
    private int category;

    /**
     * @see CashierOrderType
     */
    private int type;

    /**
     * 订单来源
     *
     * @see com.mammon.enums.CommonSource
     */
    private int source;

    /**
     * 抹零类型, 0:不抹零，1：抹分，2：抹角，3：元取整
     */
    private int ignoreType;

    /**
     * 折扣
     */
    private BigDecimal discount = BigDecimal.ZERO;

    /**
     * 会员信息
     */
    private String memberId;

    /**
     * 整单备注
     */
    private String remark;

    /**
     * 服务人员
     */
    private List<String> serviceAccountIds;

    /**
     * 调整金额(手动加减价)
     */
    private BigDecimal adjustAmount = BigDecimal.ZERO;

    /**
     * 计次核销次数
     */
    private long countedTotal;

    /**
     * 销售日期
     */
    private LocalDateTime cashierTime;

    /**
     * 购物车信息
     */
    private List<CashierOrderSkuDto> cards = new ArrayList<>();
}
