package com.mammon.merchant.domain.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MerchantVo {

    private long merchantNo;

    private String merchantName;

    private String pcLogo;

    private String mobileLogo;

    private String accountId;

    private String accountName;

    private String accountPhone;

    /**
     * 注册地址
     */
    private String province;

    private String city;

    private String area;

    private String address;

    /**
     * 注册日期
     */
    private LocalDateTime createTime;

    /**
     * 版本信息
     */
    private String industryId;

    private String industryName;

    private int industryType;

    /**
     * 商户版本到期日期
     */
    private LocalDate expireDate;

    /**
     * 商户类型
     */
    private int type;
}
