package com.mammon.clerk.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserDto {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[1][3-9][0-9]{9}$", message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String captcha;

    /**
     * 三天内免登
     */
    private boolean rememberMe;

    private int platform;
}
