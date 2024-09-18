package com.mammon.sms.service;

import com.mammon.sms.dao.SmsChannelDao;
import com.mammon.sms.domain.entity.SmsChannelEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SmsChannelService {

    @Resource
    private SmsChannelDao smsChannelDao;

    public SmsChannelEntity findBySmsType(int smsType) {
        return smsChannelDao.findBySmsType(smsType);
    }

    public SmsChannelEntity findById(String id) {
        return smsChannelDao.findById(id);
    }
}
