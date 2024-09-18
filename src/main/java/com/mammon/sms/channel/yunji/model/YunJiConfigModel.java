package com.mammon.sms.channel.yunji.model;

import lombok.Data;

/**
 * @author dcl
 * @date 2022-10-13 11:47:58
 */
@Data
public class YunJiConfigModel {

    /**
     * 调用账号
     */
    private String accessKey;

    /**
     * 调用秘钥
     */
    private String accessSecret;

    /**
     * 分类秘钥
     */
    private String classificationSecret;

    /**
     * 短信分类
     */
    private int classify;
}
