package com.mammon.auth.channel.wechat.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * @author dcl
 * @since 2024/8/6 14:31
 */
@Data
public class TradeWechatBaseVo {

    @JsonAlias("errcode")
    private int errCode;

    @JsonAlias("errmsg")
    private String errMsg;
}
