package com.mammon.print.channel.gain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mammon.print.channel.elind.model.ElindBasicPrintDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @since 2024/2/26 14:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GainPrintInfoDto extends GainBasicPrintDto {

    @JsonProperty("deviceID")
    private String deviceId;
}
