package com.mammon.print.channel.elind.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ElindAddPrintDto extends ElindBasicPrintDto {

    /**
     * 打印机终端号
     */
    @JsonProperty("machine_code")
    private String machineCode;

    /**
     * 终端秘钥
     */
    private String msign;

    /**
     * 服务授权值
     */
    @JsonProperty("access_token")
    private String accessToken;
}
