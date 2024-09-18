package com.mammon.print.channel.elind.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dcl
 * @since 2024/2/26 15:04
 */
@Data
public class ElindPrintInfoVo {

    private String version;

    @JsonProperty("print_width")
    private String width;
}
