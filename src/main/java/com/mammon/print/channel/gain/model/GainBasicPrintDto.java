package com.mammon.print.channel.gain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GainBasicPrintDto {

    /**
     * 时间戳
     */
    private String reqTime;

    /**
     * 安全校验码
     */
    private String securityCode;

    /**
     * 商户编码
     */
    private String memberCode;
}
