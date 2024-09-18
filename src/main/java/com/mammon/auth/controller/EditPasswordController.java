package com.mammon.auth.controller;

import com.mammon.auth.service.SmsCaptchaService;
import com.mammon.common.ResultJson;
import com.mammon.clerk.domain.dto.OriginalPasswordResetDto;
import com.mammon.auth.domain.dto.SmsCaptchaResetDto;
import com.mammon.auth.service.RestPasswordService;
import com.mammon.sms.enums.SmsTempTypeEnum;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/oauth/edit-password")
public class EditPasswordController {

    @Resource
    private RestPasswordService resetPasswordService;

    @Resource
    private SmsCaptchaService smsCaptchaService;

    /**
     * 发送短信验证码
     *
     * @param phone 手机号码
     * @return 返回ResultJson对象，表示操作结果
     */
    @PostMapping("/sms-captcha/{phone}")
    public ResultJson<Void> smsCaptcha(@PathVariable("phone") String phone) {
        smsCaptchaService.sendSmsCaptcha(phone, SmsTempTypeEnum.EDIT_PASSWORD.getCode());
        return ResultJson.ok();
    }

    /**
     * 验证码方式修改密码
     *
     * @param dto
     * @return
     */
    @PostMapping("/sms-captcha-reset")
    public ResultJson<Void> smsCaptchaReset(@RequestBody SmsCaptchaResetDto dto) {
        resetPasswordService.reset(dto);
        return ResultJson.ok();
    }

    /**
     * 原密码方式修改密码
     *
     * @return
     */
    @PostMapping("/original-password-reset")
    public ResultJson<Void> originalPasswordReset(@RequestBody OriginalPasswordResetDto dto) {
        resetPasswordService.reset(dto);
        return ResultJson.ok();
    }
}
