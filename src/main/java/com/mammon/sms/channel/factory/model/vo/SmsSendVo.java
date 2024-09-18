package com.mammon.sms.channel.factory.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SmsSendVo {

    private String smsSendId;

    private int status;

    private String errorDesc;

    private List<SmsDetailVo> resDetail = new ArrayList<>();
}
