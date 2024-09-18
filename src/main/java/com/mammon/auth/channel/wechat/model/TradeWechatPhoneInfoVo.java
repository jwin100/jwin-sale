package com.mammon.auth.channel.wechat.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @since 2024/8/6 14:32
 */
@Data
public class TradeWechatPhoneInfoVo {

    /**
     * 用户绑定的手机号(国外的会有区号)
     */
    private String phoneNumber;

    /**
     * 没有区号的手机号
     */
    private String purePhoneNumber;

    /**
     * 区号
     */
    private String countryCode;
}
