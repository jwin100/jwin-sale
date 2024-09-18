package com.mammon.sms.channel.yunji.model.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dcl
 * @date 2022-10-13 11:10:14
 */
@Data
public class YunJiSendRequestDto {

    /**
     * 调用账号
     */
    private String accessKey;

    /**
     * 调用秘钥
     */
    private String accessSecret;

    /**
     * 签名code
     */
    private String signCode;

    /**
     * 模板code
     */
    private String templateCode;

    /**
     * 套餐分类秘钥
     */
    private String classificationSecret;

    /**
     * 内容参数
     */
    private Map<String, Object> params = new HashMap<>();
}
