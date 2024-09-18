package com.mammon.clerk.domain.dto;

import lombok.Data;

@Data
public class AccountDto {
    /**
     * 角色
     */
    private String roleId;

    /**
     * 所属门店
     */
    private long storeNo;

    private String name;

    private String phone;

    private String email;

    private String password;

    private String openId;
}
