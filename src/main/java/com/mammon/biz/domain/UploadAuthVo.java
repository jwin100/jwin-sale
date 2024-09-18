package com.mammon.biz.domain;

import lombok.Data;

@Data
public class UploadAuthVo {

    /**
     * 路径信息
     */
    private String policy;

    /**
     * 授权码
     */
    private String authorization;

    /**
     * 过期时间戳(10位)
     */
    private long expire;
}
