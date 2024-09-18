package com.mammon.sms.channel.fish.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @since 2023/12/11 17:59
 */
@Data
public class FishBatchSendDto {

    private String account;

    private String password;

    /**
     * 最多200个
     */
    private List<FishBatchSendItemDto> mobiles = new ArrayList<>();

    private String subCode;

    private String smsId;

    private String sendTime;
}
