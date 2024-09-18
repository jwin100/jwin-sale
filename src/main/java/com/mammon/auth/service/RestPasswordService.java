package com.mammon.auth.service;

import com.mammon.exception.CustomException;
import com.mammon.clerk.domain.dto.OriginalPasswordResetDto;
import com.mammon.auth.domain.dto.SmsCaptchaResetDto;
import com.mammon.clerk.domain.entity.AccountEntity;
import com.mammon.clerk.service.AccountService;
import com.mammon.service.RedisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RestPasswordService {
    @Resource
    private AccountService accountService;

    @Resource
    private RedisService redisService;

    @Resource
    private SmsCaptchaService smsCaptchaService;

    /**
     * 重置用户密码
     *
     * @param dto 包含重置密码所需信息的DTO对象
     *
     * @throws CustomException 如果两次输入的密码不一致或手机号信息错误，则抛出此异常
     */
    public void reset(SmsCaptchaResetDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new CustomException("两次密码不一致");
        }
        smsCaptchaService.validSmsCaptcha(dto.getPhone(), dto.getSmsCaptcha());
        AccountEntity account = accountService.findByPhone(dto.getPhone());
        if (account == null) {
            throw new CustomException("修改失败：手机号信息错误");
        }
        String password = accountService.encodePassword(dto.getPassword());
        accountService.editPassword(account.getId(), null, password);
    }

    /**
     * 重置用户密码
     *
     * @param dto 包含重置密码所需信息的DTO对象
     *
     * @throws CustomException 如果两次输入的密码不一致、手机号信息错误或原密码错误，则抛出此异常
     */
    public void reset(OriginalPasswordResetDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new CustomException("两次密码不一致");
        }
        AccountEntity account = accountService.findByPhone(dto.getPhone());
        if (account == null) {
            throw new CustomException("修改失败：不存在不存在");
        }
        String originalPassword = accountService.encodePassword(dto.getOriginalPassword());
        if (originalPassword.equals(account.getPassword())) {
            throw new CustomException("修改失败：原密码错误");
        }
        String password = accountService.encodePassword(dto.getPassword());
        accountService.editPassword(account.getId(), account.getPassword(), password);
    }
}
