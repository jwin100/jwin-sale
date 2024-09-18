package com.mammon.auth.domain.dto;

import lombok.Data;

@Data
public class SmsCaptchaResetDto {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 短信验证码
     */
    private String smsCaptcha;

    /**
     * 新密码
     */
    private String password;

    /**
     * 确认新密码
     */
    private String confirmPassword;
}
