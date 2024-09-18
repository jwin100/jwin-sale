package com.mammon.office.order.channel.alipay.model;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-02-02 16:12:36
 */
@Data
public class AlipayConfigModel {

    private String appId;

    /**
     * 应用私钥
     */
    private String privateKey;

    /**
     * 支付宝公钥
     */
    private String alipayPublicKey;

    /**
     * 加密方式
     */
    private String signType;

    /**
     * 回调url
     */
    private String notifyUrl;
}
