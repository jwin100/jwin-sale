package com.mammon.sms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SmsSendUserDto {

    private String userId;

    private String phone;
}
