package com.mammon.market.domain.vo;

import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class MarketTimeCardRuleListVo {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String name;

    /**
     * 有效期(0:永久有效,1:开卡时+结束日期)
     */
    private int expireType;

    private long expireMonth;

    /**
     * 计次次数
     */
    private int timeTotal;

    /**
     * 售价
     */
    private BigDecimal realAmount;

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

    private List<String> spuIds = new ArrayList<>();

    private List<MarketTimeCardSpuVo> spus = new ArrayList<>();

    private int status;

    private String statusName;

    private int sort;

    private String remark;

    private String accountId;

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), CommonStatus.class);
    }
}
