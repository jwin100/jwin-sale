package com.mammon.sms.channel.factory.model.vo;

import lombok.Data;

@Data
public class SmsDetailVo {

    private String phone;

    private int status;

    private String errorDesc;
}
