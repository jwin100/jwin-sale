package com.mammon.office.edition.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-02-06 14:38:10
 */
@Data
public class IndustryMerchantActiveDto {

    //商户编号
    private long merchantNo;

    //开通版本号
    private String industryId;

    private Integer industryType;

    //开通时长(月)
    private long addMonth;

    private String orderId;
}
