package com.mammon.office.order.channel.wechatpay.model;

import lombok.Data;

/**
 * @author dcl
 * @since 2023/12/15 10:07
 */
@Data
public class WechatpayConfigModel {

    private String appId;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * RSA私钥
     */
    private String privateKey;

    /**
     * 证书序列号
     */
    private String serialNumber;

    /**
     * 商户APIV3密钥
     */
    private String apiV3Key;


}
