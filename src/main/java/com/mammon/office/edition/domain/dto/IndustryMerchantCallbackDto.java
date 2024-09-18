package com.mammon.office.edition.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-02-06 14:38:10
 */
@Data
public class IndustryMerchantCallbackDto {

    //商户编号
    private long merchantNo;

    //开通版本号
    private String industryId;

    //开通时长(月)
    private long callbackMonth;

    private String orderId;
}
