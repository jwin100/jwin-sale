package com.mammon.clerk.domain.dto;

import lombok.Data;

@Data
public class OriginalPasswordResetDto {

    /**
     * 手机号
     */
    private String phone;

    private String originalPassword;

    /**
     * 新密码
     */
    private String password;

    /**
     * 确认新密码
     */
    private String confirmPassword;
}
