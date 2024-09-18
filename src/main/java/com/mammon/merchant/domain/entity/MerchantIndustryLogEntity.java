package com.mammon.merchant.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 版本开通记录
 */
@Data
public class MerchantIndustryLogEntity {

    private String id;

    private long merchantNo;

    private String packageId;

    private String industryId;

    /**
     * 开通时长(月
     */
    private long addMonth;

    private int type;

    /**
     * 开通，退订记录订单号
     */
    private String orderNo;

    private LocalDateTime createTime;
}
