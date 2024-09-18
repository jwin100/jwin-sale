package com.mammon.print.channel.gain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mammon.print.channel.elind.model.ElindBasicPrintDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GainPrintDto extends GainBasicPrintDto {

    @JsonProperty("deviceID")
    private String deviceId;

    /**
     * mode=2，自由格式打印
     */
    private String mode = "2";

    /**
     * 打印信息的内容
     */
    private String msgDetail;

    /**
     * 小票打印设置1,标签打印设置4
     * <p>
     * 具体见文档
     */
    private String charset;
}
