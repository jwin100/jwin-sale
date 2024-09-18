package com.mammon.auth.channel.wechat.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @since 2024/8/6 14:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TradeWechatPhoneVo extends TradeWechatBaseVo {

    @JsonAlias("phone_info")
    private TradeWechatPhoneInfoVo phoneInfo;
}
