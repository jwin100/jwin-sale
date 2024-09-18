package com.mammon.print.channel.gain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GainAddPrintDto extends GainBasicPrintDto {

    /**
     * 终端号
     */
    @JsonProperty("deviceID")
    private String deviceId;

    /**
     * 设备名称
     */
    private String devName;
}
