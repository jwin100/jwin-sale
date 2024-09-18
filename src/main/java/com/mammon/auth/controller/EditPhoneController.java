package com.mammon.auth.controller;

import com.mammon.auth.service.SmsCaptchaService;
import com.mammon.common.ResultJson;
import com.mammon.auth.domain.dto.EditPhoneDto;
import com.mammon.auth.service.ResetPhoneService;
import com.mammon.sms.enums.SmsTempTypeEnum;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/oauth/edit-phone")
public class EditPhoneController {

    @Resource
    private ResetPhoneService resetPhoneService;

    @Resource
    private SmsCaptchaService smsCaptchaService;

    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    @PostMapping("/sms-captcha/{phone}")
    public ResultJson<Void> smsCaptcha(@PathVariable("phone") String phone) {
        smsCaptchaService.sendSmsCaptcha(phone, SmsTempTypeEnum.EDIT_PHONE.getCode());
        return ResultJson.ok();
    }

    /**
     * 验证码重置
     *
     * @param dto
     * @return
     */
    @PostMapping("/reset")
    public ResultJson<Void> reset(@RequestBody EditPhoneDto dto) {
        resetPhoneService.reset(dto);
        return ResultJson.ok();
    }
}
