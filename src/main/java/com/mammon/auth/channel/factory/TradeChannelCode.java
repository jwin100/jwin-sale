package com.mammon.auth.channel.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2024/8/6 10:40
 */
@Getter
@AllArgsConstructor
public enum TradeChannelCode {
    WECHAT("wechat"),
    ;

    private final String code;
}
