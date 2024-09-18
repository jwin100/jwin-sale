package com.mammon.auth.domain.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/5/23 13:53
 */
@Data
public class AuthScanLoginStatusVo {

    /**
     * 扫码状态
     */
    private int status;

    private String statusName;

    /**
     * 登录信息
     */
    private LoginVo loginVo;
}
