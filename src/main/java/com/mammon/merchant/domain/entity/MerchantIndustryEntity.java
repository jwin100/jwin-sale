package com.mammon.merchant.domain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 商户版本
 */
@Data
public class MerchantIndustryEntity {

    private String id;

    private long merchantNo;

    /**
     * 版本id
     */
    private String industryId;

    /**
     * 到期日期
     */
    private LocalDate expireDate;

    /**
     * 开通类型
     */
    private int type;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
