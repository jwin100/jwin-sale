package com.mammon.print.channel.gain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @since 2024/2/26 15:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GainPrintInfoVo extends GainBasePrintVo {

    private GainPrintInfoItemVo devInfo;
}