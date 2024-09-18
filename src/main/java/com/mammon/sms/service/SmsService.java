package com.mammon.sms.service;

import com.mammon.common.Generate;
import com.mammon.exception.CustomException;
import com.mammon.clerk.domain.vo.UserVo;
import com.mammon.clerk.service.AccountService;
import com.mammon.sms.dao.SmsDao;
import com.mammon.sms.domain.dto.SmsRechargeLogDto;
import com.mammon.sms.domain.entity.SmsEntity;
import com.mammon.sms.enums.SmsRechargeLogTypeConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
@Slf4j
public class SmsService {

    private static final int GIVE_SMS_CNT = 30;

    @Resource
    private SmsDao smsDao;

    @Resource
    private SmsRechargeLogService smsRechargeLogService;

    @Resource
    private SmsTemplateSettingService smsTemplateSettingService;

    public int smsExists(long merchantNo, long storeNo, String accountId) {
        return smsInfo(merchantNo) != null ? 1 : 0;
    }

    @Transactional(rollbackFor = CustomException.class)
    public void smsEnable(long merchantNo, long storeNo, String accountId) {
        SmsEntity entity = new SmsEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setRecharge(0);
        entity.setStatus(1);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        smsDao.save(entity);
//        UserVo user = accountService.info(accountId);
//        if (user != null && StringUtils.isNotBlank(user.getMerchantName())) {
//            smsSignService.init(merchantNo, storeNo, accountId, user.getMerchantName());
//        }
        smsTemplateSettingService.init(merchantNo);
        smsRechargeChange(merchantNo, storeNo, SmsRechargeLogTypeConst.赠送, null, null,
                GIVE_SMS_CNT, "首次开通赠送");
    }

    public SmsEntity smsInfo(long merchantNo) {
        return smsDao.findByMerchantNo(merchantNo);
    }

    /**
     * 充值
     *
     * @param merchantNo
     * @param storeNo
     * @param remark
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public int smsRechargeChange(long merchantNo, long storeNo, int changeType,
                                 String sendId, String orderId, long rechargeCnt, String remark) {
        int changeStatus = 1;
        SmsEntity sms = smsDao.updateRecharge(merchantNo, rechargeCnt);
        if (sms == null) {
            changeStatus = 0;
        }
        SmsRechargeLogDto logDto = new SmsRechargeLogDto();
        logDto.setChangeIn(rechargeCnt);
        logDto.setChangeAfter(sms != null ? sms.getRecharge() : 0);
        logDto.setChangeStatus(changeStatus > 0 ? 1 : 0);
        logDto.setChangeType(changeType);
        logDto.setSendId(sendId);
        logDto.setOrderNo(orderId);
        logDto.setRemark(remark);
        smsRechargeLogService.save(merchantNo, storeNo, logDto);
        return changeStatus;
    }
}
