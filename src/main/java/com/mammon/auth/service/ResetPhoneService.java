package com.mammon.auth.service;

import com.mammon.exception.CustomException;
import com.mammon.merchant.service.MerchantService;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.auth.domain.dto.EditPhoneDto;
import com.mammon.clerk.domain.entity.AccountEntity;
import com.mammon.clerk.service.AccountService;
import com.mammon.service.RedisService;
import com.mammon.sms.service.SmsSendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class ResetPhoneService {
    @Resource
    private AccountService accountService;

    @Resource
    private SmsSendService smsSendService;

    @Resource
    private RedisService redisService;

    @Resource
    private MerchantService merchantService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private SmsCaptchaService smsCaptchaService;

    /**
     * 重置手机号
     *
     * @param dto 包含新手机号和短信验证码的DTO对象
     *
     * @throws CustomException 如果手机号信息错误则抛出此异常
     *
     * @Transactional(rollbackFor = Exception.class) 使用事务注解，当方法内发生异常时，将回滚事务
     */
    @Transactional(rollbackFor = Exception.class)
    public void reset(EditPhoneDto dto) {
        smsCaptchaService.validSmsCaptcha(dto.getNewPhone(), dto.getSmsCaptcha());
        AccountEntity account = accountService.findByPhone(dto.getPhone());
        if (account == null) {
            throw new CustomException("修改失败：手机号信息错误");
        }
        accountService.editPhone(account.getId(), dto.getPhone(), dto.getNewPhone());
    }
}
