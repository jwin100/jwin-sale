package com.mammon.sms.channel.yunji.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dcl
 * @date 2022-10-13 11:20:18
 */
@Data
public class YunJiBatchSendVo {

    @JsonProperty("Timestamps")
    private long timestamps;

    @JsonProperty("BusinessData")
    private YunJiBusinessDataVo businessData;

    @JsonProperty("BusinessException")
    private YunJiBusinessExceptionVo businessException;
}
