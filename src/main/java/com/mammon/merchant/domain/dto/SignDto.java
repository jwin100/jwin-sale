package com.mammon.merchant.domain.dto;

import lombok.Data;

@Data
public class SignDto {

    private String merchantName;

    private String name;

    private String phone;

    private String email;

    private String password;

    /**
     * 注册地址
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

    private String inviteCode;
}
