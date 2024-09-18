package com.mammon.goods.channel.wanwei.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dcl
 * @since 2024/4/11 18:32
 */
@Data
public class WanWeiBarCodeVo {

    @JsonProperty("showapi_res_code")
    private int showApiResCode;

    @JsonProperty("showapi_res_error")
    private String showApiResError;

    @JsonProperty("showapi_res_body")
    private WanWeiBarcodeDetailVo showApiResBody;
}
