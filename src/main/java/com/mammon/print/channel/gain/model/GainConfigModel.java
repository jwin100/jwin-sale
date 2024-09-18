package com.mammon.print.channel.gain.model;

import lombok.Data;

@Data
public class GainConfigModel {

    /**
     * api编码
     */
    private String clientId;

    /**
     * api秘钥
     */
    private String clientSecret;

    /**
     * 授权码
     */
    private String accessToken;
}
