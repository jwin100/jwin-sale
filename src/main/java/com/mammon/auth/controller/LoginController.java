package com.mammon.auth.controller;

import com.mammon.auth.domain.UserDetail;
import com.mammon.auth.domain.dto.LoginDto;
import com.mammon.auth.domain.dto.MiniAppLoginDto;
import com.mammon.auth.domain.dto.MiniAppPhoneLoginDto;
import com.mammon.auth.domain.vo.*;
import com.mammon.auth.service.SmsCaptchaService;
import com.mammon.common.ResultJson;
import com.mammon.auth.service.AuthService;
import com.mammon.sms.enums.SmsTempTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Detainted;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/oauth/login")
@Validated
@Slf4j
public class LoginController {

    @Resource
    private AuthService authService;

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
     * 登录
     *
     * @param request
     * @param dto
     * @return
     */
    @PostMapping
    public ResultJson<LoginVo> login(HttpServletRequest request, @RequestBody LoginDto dto) {
        return ResultJson.ok(authService.login(request, dto));
    }

    /**
     * 登录验证
     *
     * @param token
     * @return
     */
    @PostMapping("/valid/{token}")
    public ResultJson<UserDetail> loginValid(@PathVariable("token") String token) {
        return ResultJson.ok(authService.validateToken(token));
    }

    /**
     * 公众平台小程序登录
     *
     * @param channelCode
     * @param dto
     * @return
     */
    @PostMapping("/{channelCode}")
    public ResultJson<MiniAppLoginVo> miniappLogin(@PathVariable("channelCode") String channelCode,
                                                   @RequestBody MiniAppLoginDto dto,
                                                   HttpServletRequest request) {
        return ResultJson.ok(authService.miniappLogin(request, channelCode, dto));
    }

    /**
     * 公众平台手机号快速验证登录
     *
     * @param channelCode 登录渠道
     * @param dto
     * @return
     */
    @PostMapping("/phone/{channelCode}")
    public ResultJson<MiniAppLoginVo> miniappPhoneLogin(@PathVariable("channelCode") String channelCode,
                                                        @RequestBody MiniAppPhoneLoginDto dto,
                                                        HttpServletRequest request) {
        return ResultJson.ok(authService.miniappPhoneLogin(request, channelCode, dto));
    }
}
