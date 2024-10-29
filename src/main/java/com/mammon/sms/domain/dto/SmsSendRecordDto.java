package com.mammon.sms.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SmsSendRecordDto {

    private String signId;

    private String sendMessage;

    private Integer tempType;

    private LocalDateTime sendTime;

    private List<SmsSendUserDto> users = new ArrayList<>();
}
