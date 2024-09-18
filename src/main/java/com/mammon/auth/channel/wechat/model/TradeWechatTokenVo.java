package com.mammon.auth.channel.wechat.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @since 2024/8/6 14:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TradeWechatTokenVo extends TradeWechatBaseVo {

    @JsonAlias("access_token")
    private String accessToken;

    @JsonAlias("expires_in")
    private long expiresIn;
}
