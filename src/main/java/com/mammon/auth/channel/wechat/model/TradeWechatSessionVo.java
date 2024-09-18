package com.mammon.auth.channel.wechat.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @since 2023/12/19 18:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TradeWechatSessionVo extends TradeWechatBaseVo {

    @JsonAlias("session_key")
    private String sessionKey;

    @JsonAlias("unionid")
    private String unionId;

    @JsonAlias("openid")
    private String openId;
}
