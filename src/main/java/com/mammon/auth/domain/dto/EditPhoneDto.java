package com.mammon.auth.domain.dto;

import lombok.Data;

@Data
public class EditPhoneDto {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 短信验证码
     */
    private String smsCaptcha;

    /**
     * 新号码
     */
    private String newPhone;
}
