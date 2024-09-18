package com.mammon.sms.domain.dto;

import lombok.Data;

@Data
public class SmsRechargeChangeDto {
    private int changeType;
    private String sendId;
    private String orderNo;
    private long rechargeCnt;
    private String remark;
}
