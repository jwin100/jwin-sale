package com.mammon.clerk.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 扫码登录
 *
 * @author dcl
 * @since 2024/5/23 10:37
 */
@Data
public class AccountScanEntity {

    private String id;

    /**
     * 扫码编号
     */
    private String scanNo;

    /**
     * 场景值
     */
    private String openId;

    /**
     * 登录信息（json）
     */
    private String loginInfo;

    /**
     * 扫码场景(1,微信,2+:xx app 百度)
     */
    private int source;

    /**
     * 状态,1:创建，2:已扫码，3:已过期
     */
    private int status;

    /**
     * 13位时间戳过期时间
     */
    private long expireTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
