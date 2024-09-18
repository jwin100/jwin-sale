package com.mammon.merchant.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MerchantIndustryLogDto {

    private long merchantNo;

    private long storeNo;

    private long changeBefore;

    private long changeIn;

    private long changeAfter;

    private int changeStatus;

    /**
     * 0:减扣,1:增加
     */
    private int changeType;

    private long bindStoreNo;

    private String orderNo;

    private String remark;

    private LocalDateTime createTime;
}
