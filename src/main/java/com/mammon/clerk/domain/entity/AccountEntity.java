package com.mammon.clerk.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登陆信息
 */
@Data
public class AccountEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String username;

    private String password;

    private String name;

    private String phone;

    private String email;

    private int mobileCashMode;

    private String openId;

    private int status;

    private int deleted;
    
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
