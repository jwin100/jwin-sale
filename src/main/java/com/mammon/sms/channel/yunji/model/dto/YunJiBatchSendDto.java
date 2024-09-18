package com.mammon.sms.channel.yunji.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @date 2022-10-13 11:15:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class YunJiBatchSendDto extends YunJiSendRequestDto {

    /**
     * 唯一任务id
     */
    private String taskId;

    private List<String> phones = new ArrayList<>();
}
