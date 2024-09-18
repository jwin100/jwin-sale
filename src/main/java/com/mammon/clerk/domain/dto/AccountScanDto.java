package com.mammon.clerk.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/5/23 11:41
 */
@Data
public class AccountScanDto {

    /**
     * 扫码场景(1,微信,2+:xx app 百度)
     */
    private int source;
}
