package com.mammon.auth.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/5/23 13:49
 */
@Data
public class LoginScanDto {

    /**
     * 请求来源（5：微信端扫码登录）
     */
    private int source;
}
