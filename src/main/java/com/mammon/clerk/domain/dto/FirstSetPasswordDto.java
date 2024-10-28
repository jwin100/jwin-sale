package com.mammon.clerk.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author dcl
 * @since 2024/3/11 10:52
 */
@Data
public class FirstSetPasswordDto {

    /**
     * 商户名
     */
    private String merchantName;

    /**
     * 姓名
     */
    private String name;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    private String password;

    /**
     * 确认新密码
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}
