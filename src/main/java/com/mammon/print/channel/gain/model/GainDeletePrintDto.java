package com.mammon.print.channel.gain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mammon.print.channel.elind.model.ElindBasicPrintDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GainDeletePrintDto extends GainBasicPrintDto {

    @JsonProperty("deviceID")
    private String deviceId;

}
