package com.mammon.sms.domain.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author dcl
 * @since 2024/6/19 10:24
 */
@Data
public class SmsSendNoticeDto {

    private long merchantNo;

    private long storeNo;

    private String accountId;

    /**
     * 发送短信模板类型
     */
    private int tempType;

    /**
     * 发送目标
     */
    private List<SmsSendUserDto> users;

    /**
     * 替换参数信息
     */
    private Map<String, String> tempParams;
}
