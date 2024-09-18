package com.mammon.print.channel.elind.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ElindPrintDto extends ElindBasicPrintDto {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("machine_code")
    private String machineCode;

    private String content;

    /**
     * 非必填(为 1 时，origin_id 进行幂等处理
     */
    private int idempotence;

    /**
     * 商户订单号
     */
    @JsonProperty("origin_id")
    private String originId;
}
