package com.mammon.print.channel.elind.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ElindBasicPrintDto {

    private String sign;

    /**
     * uuid
     */
    private String id;

    /**
     * 10位时间戳
     */
    private int timestamp;

    /**
     * 应用id
     */
    @JsonProperty("client_id")
    private String clientId;
}
