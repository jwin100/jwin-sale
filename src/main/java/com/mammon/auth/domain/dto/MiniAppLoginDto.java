package com.mammon.auth.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/8/6 15:25
 */
@Data
public class MiniAppLoginDto {

    /**
     * 授权登录code
     */
    private String code;

    private int source;
}
