package com.mammon.auth.channel.wechat.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * @author dcl
 * @since 2023/12/19 18:39
 */
@Data
public class TradeWechatSessionDto {

    @JsonAlias("appid")
    private String appId;

    private String secret;

    @JsonAlias("js_code")
    private String jsCode;

    @JsonAlias("grant_type")
    private String grantType;
}
