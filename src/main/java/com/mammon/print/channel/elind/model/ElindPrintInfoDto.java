package com.mammon.print.channel.elind.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @since 2024/2/26 14:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElindPrintInfoDto extends ElindBasicPrintDto {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("machine_code")
    private String machineCode;
}
