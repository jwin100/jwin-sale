package com.mammon.auth.domain.dto;

import lombok.Data;

/**
 * 手机号快速验证登录
 *
 * @author dcl
 * @since 2024/8/6 15:27
 */
@Data
public class MiniAppPhoneLoginDto {

    /**
     * 用户openId
     */
    private String openId;

    /**
     * 获取手机号授权code
     */
    private String code;

    private int source;
}
