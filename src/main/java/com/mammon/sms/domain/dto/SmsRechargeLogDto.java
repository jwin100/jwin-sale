package com.mammon.sms.domain.dto;

import lombok.Data;

@Data
public class SmsRechargeLogDto {

    private long merchantNo;

    private long storeNo;

    private long changeBefore;

    private long changeIn;

    private long changeAfter;

    private int changeStatus;

    /**
     * @see com.mammon.sms.enums.SmsRechargeLogTypeConst
     */
    private int changeType;

    /**
     * 关联sendId方便定位
     */
    private String sendId;

    private String orderNo;

    private String remark;
}
