package com.mammon.sms.service;

import com.mammon.sms.dao.SmsTemplateChannelRelDao;
import com.mammon.sms.domain.entity.SmsTemplateChannelRelEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SmsTemplateChannelRelService {

    @Resource
    private SmsTemplateChannelRelDao smsTemplateChannelRelDao;

    public SmsTemplateChannelRelEntity findByTempIdAndChannelId(String tempId, String channelId) {
        return smsTemplateChannelRelDao.findByTempIdAndChannelId(tempId, channelId);
    }
}
