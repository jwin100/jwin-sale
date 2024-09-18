package com.mammon.sms.channel.factory.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SmsSendDto {

    private String smsSendId;

    private String configStr;

    private int smsType;

    private List<String> phone = new ArrayList<>();

    private String content;

    private LocalDateTime sendTime;

    private String signCode;

    private String tempCode;

    private Map<String, Object> params = new HashMap<>();
}
