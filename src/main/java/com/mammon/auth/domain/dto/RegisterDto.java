package com.mammon.auth.domain.dto;

import lombok.Data;

@Data
public class RegisterDto {

    /**
     * 授权类型，支持的授权类型：password、refresh_token、sms_code
     */
    private String grantType;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 短信验证码
     */
    private String smsCaptcha;

    private int source;

    private String openId;
}
