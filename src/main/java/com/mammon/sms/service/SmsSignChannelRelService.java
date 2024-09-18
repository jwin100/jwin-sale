package com.mammon.sms.service;

import com.mammon.sms.dao.SmsSignChannelRelDao;
import com.mammon.sms.domain.entity.SmsSignChannelRelEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SmsSignChannelRelService {

    @Resource
    private SmsSignChannelRelDao smsSignChannelRelDao;

    public SmsSignChannelRelEntity findBySignIdAndChannelId(String signId, String channelId) {
        return smsSignChannelRelDao.findBySignIdAndChannelId(signId, channelId);
    }
}
