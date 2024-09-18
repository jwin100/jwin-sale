package com.mammon.print.channel.gain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class GainStatusItemVo {

    @JsonProperty("deviceID")
    private String deviceId;

    private int online;

    private int status;

    @JsonProperty("outtime")
    private String outTime;
}
