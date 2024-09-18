package com.mammon.auth.domain.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2023/9/28 16:32
 */
@Data
public class LoginVo {

    /**
     * 是否新注册
     */
    private boolean register;

    private String accessToken;

    private String refreshToken;
}
