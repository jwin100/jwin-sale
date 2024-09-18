package com.mammon.sms.service;

import com.mammon.common.Generate;
import com.mammon.sms.dao.SmsRechargeLogDao;
import com.mammon.sms.domain.dto.SmsRechargeLogDto;
import com.mammon.sms.domain.entity.SmsRechargeLogEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class SmsRechargeLogService {

    @Resource
    private SmsRechargeLogDao smsRechargeLogDao;

    public int save(long merchantNo, long storeNo, SmsRechargeLogDto dto) {
        SmsRechargeLogEntity entity = new SmsRechargeLogEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setCreateTime(LocalDateTime.now());
        return smsRechargeLogDao.save(entity);
    }
}
