package com.mammon.merchant.domain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 门店信息
 */
@Data
public class MerchantStoreEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String storeName;

    private String storePhone;

    private String accountId;

    /**
     * 是否主店
     */
    private boolean main;

    /**
     * 额度到期日期
     */
    private LocalDate endDate;

    /**
     * 门店地址
     */
    private String province;

    private String city;

    private String area;

    private String address;

    /**
     * 行业分类
     */
    private Long industryOne;

    private Long industryTwo;

    private Long industryThree;

    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}