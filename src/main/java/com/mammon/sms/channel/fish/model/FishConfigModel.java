package com.mammon.sms.channel.fish.model;

import lombok.Data;

@Data
public class FishConfigModel {

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    private String smsType;

    private String version;

    private int subCode;
}
