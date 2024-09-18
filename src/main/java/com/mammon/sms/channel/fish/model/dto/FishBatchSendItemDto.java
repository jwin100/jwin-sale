package com.mammon.sms.channel.fish.model.dto;

import lombok.Data;

/**
 * @author dcl
 * @since 2023/12/11 17:59
 */
@Data
public class FishBatchSendItemDto {

    private String mobile;

    private String content;
}
