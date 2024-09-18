package com.mammon.sms.channel.fish.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FishSendDto {

    private String account;

    private String password;

    private String smsType;

    private String mobile;

    private String content;

    private String subCode;

    private String version;

    private String smsId;

    private String sendTime;
}
