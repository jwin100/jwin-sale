package com.mammon.clerk.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StoreSignDto {

    private long merchantNo;

    private long storeNo;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String roleId;

    private String name;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    private String email;
}