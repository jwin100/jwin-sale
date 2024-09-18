package com.mammon.merchant.domain.dto;

import lombok.Data;

@Data
public class MerchantStoreDto {

    private String storeName;

    private String storePhone;

    private String accountId;

    private String roleId;

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
}
