package com.mammon.auth.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2023/9/28 16:17
 */
@Data
public class LoginDto {

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
     * 密码
     */
    private String password;

    /**
     * 短信验证码
     */
    private String smsCaptcha;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 平台类型
     */
    private int platform;

    private String openId;
}
