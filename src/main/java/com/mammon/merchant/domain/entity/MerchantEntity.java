package com.mammon.merchant.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商户信息
 */
@Data
public class MerchantEntity {

    private long merchantNo;

    private String merchantName;

    private String pcLogo;

    private String mobileLogo;

    private String accountId;

    /**
     * 注册地址
     */
    private String province;

    private String city;

    private String area;

    private String address;

    private String agentId;

    private int status;

    private int type;

    private String code;

    private int source;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
