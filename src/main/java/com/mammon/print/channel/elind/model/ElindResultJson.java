package com.mammon.print.channel.elind.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ElindResultJson<T> {

    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

    private T body;
}
