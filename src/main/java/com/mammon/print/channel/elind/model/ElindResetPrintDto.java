package com.mammon.print.channel.elind.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ElindResetPrintDto extends ElindBasicPrintDto {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("machine_code")
    private String machineCode;

    /**
     * 重启:restart,关闭:shutdown
     */
    @JsonProperty("response_type")
    private String responseType;
}
