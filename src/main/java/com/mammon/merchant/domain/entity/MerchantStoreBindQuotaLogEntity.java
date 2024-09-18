package com.mammon.merchant.domain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 门店绑定额度记录
 *
 * @author dcl
 * @date 2023-03-03 11:49:26
 */
@Data
public class MerchantStoreBindQuotaLogEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    private LocalDate beforeEndDate;

    /**
     * 绑定时长(天
     */
    private long addDays;

    private LocalDate afterEndDate;

    private LocalDateTime createTime;
}
