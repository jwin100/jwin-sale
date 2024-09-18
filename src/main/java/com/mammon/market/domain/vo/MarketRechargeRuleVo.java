package com.mammon.market.domain.vo;

import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2023/7/31 15:02
 */
@Data
public class MarketRechargeRuleVo {

    private String id;

    private long merchantNo;

    private long storeNo;

    /**
     * 储值金额(不重复)
     */
    private BigDecimal prepaidAmount;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 售价
     */
    private BigDecimal realAmount;

    /**
     * 赠送金额
     */
    private BigDecimal giveAmount;

    /**
     * 送积分
     */
    private long giveIntegral;

    /**
     * 送优惠券id
     */
    private String giveCouponId;

    /**
     * 送优惠券数量
     */
    private long giveCouponTotal;

    private int status;

    private String statusName;

    private int sort;

    private String remark;

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), CommonStatus.class);
    }
}
