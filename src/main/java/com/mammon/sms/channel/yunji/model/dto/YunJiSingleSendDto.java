package com.mammon.sms.channel.yunji.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @date 2022-10-13 11:14:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class YunJiSingleSendDto extends YunJiSendRequestDto {


    /**
     * 目标手机
     */
    private String phone;
}
