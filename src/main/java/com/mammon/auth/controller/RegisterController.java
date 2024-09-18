package com.mammon.auth.controller;

import com.mammon.auth.domain.vo.LoginVo;
import com.mammon.auth.service.SmsCaptchaService;
import com.mammon.common.ResultJson;
import com.mammon.auth.domain.dto.RegisterDto;
import com.mammon.auth.service.GenerateService;
import com.mammon.sms.enums.SmsTempTypeEnum;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/oauth/register")
public class RegisterController {

    @Resource
    private GenerateService generateService;

    @Resource
    private SmsCaptchaService smsCaptchaService;

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @PostMapping("/sms-captcha/{phone}")
    public ResultJson<Void> sendSmsCode(@PathVariable("phone") String phone) {
        smsCaptchaService.sendSmsCaptcha(phone, SmsTempTypeEnum.LOGIN.getCode());
        return ResultJson.ok();
    }

    /**
     * 注册
     *
     * @param request
     * @param dto
     * @return
     */
    @PostMapping
    public ResultJson<LoginVo> register(HttpServletRequest request, @RequestBody RegisterDto dto) {
        return ResultJson.ok(generateService.register(request, dto));
    }
}
