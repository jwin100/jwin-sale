package com.mammon.sms.channel.factory;

import com.mammon.sms.channel.factory.model.dto.BatchSmsSendDto;
import com.mammon.sms.channel.factory.model.vo.SmsCallbackVo;
import com.mammon.sms.channel.factory.model.dto.SmsSendDto;
import com.mammon.sms.channel.factory.model.vo.SmsSendVo;

import java.util.Map;

public interface SmsChannelFactory {

    SmsSendVo smsSend(SmsSendDto dto);

    SmsSendVo batchSmsSend(BatchSmsSendDto dto);

    SmsCallbackVo smsCallback(Map<String, String> params);

    Object callbackSuccess();
}
