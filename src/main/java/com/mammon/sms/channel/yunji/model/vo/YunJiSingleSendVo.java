package com.mammon.sms.channel.yunji.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dcl
 * @date 2022-10-13 11:32:09
 */
@Data
public class YunJiSingleSendVo {

    @JsonProperty("Timestamps")
    private long timestamps;

    @JsonProperty("BusinessData")
    private YunJiBusinessDataItemVo businessData;

    @JsonProperty("BusinessException")
    private YunJiBusinessExceptionVo businessException;
}
