package com.mammon.merchant.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 实名信息
 */
@Data
public class MerchantAuthEntity {

    private String id;

    private long merchantNo;

    private String name;

    private int gender;

    private String nation;

    private String birthDate;

    private String idCardNo;

    private String idCardFrontUrl;

    private String idCardBackUrl;

    private int status;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
