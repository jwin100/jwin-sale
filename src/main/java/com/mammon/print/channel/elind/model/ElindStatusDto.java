package com.mammon.print.channel.elind.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ElindStatusDto extends ElindBasicPrintDto {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("machine_code")
    private String machineCode;
}
