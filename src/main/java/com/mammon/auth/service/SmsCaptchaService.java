package com.mammon.auth.service;

import com.mammon.auth.domain.enums.CaptchaConst;
import com.mammon.common.Generate;
import com.mammon.exception.CustomException;
import com.mammon.service.RedisService;
import com.mammon.sms.domain.dto.SmsSendNoticeDto;
import com.mammon.sms.domain.dto.SmsSendUserDto;
import com.mammon.sms.service.SmsSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author dcl
 * @since 2023/11/21 14:42
 */
@Service
@Slf4j
public class SmsCaptchaService {

    @Resource
    private RedisService redisService;

    @Resource
    private SmsSendService smsSendService;

    /**
     * 发送短信码
     *
     * @param phone   手机号
     * @param smsType 短信类型
     */
    public void sendSmsCaptcha(String phone, int smsType) {
        String key = CaptchaConst.SEND_SMS_CAPTCHA + phone;
        validSmsCaptchaTotal(phone);
        String captchaCode = Generate.randomCode();
        redisService.set(key, captchaCode, 5, TimeUnit.MINUTES);

        Map<String, String> tempParams = new HashMap<>();
        tempParams.put("code", captchaCode);

        List<SmsSendUserDto> users = Collections.singletonList(new SmsSendUserDto(null, phone));
        try {
            SmsSendNoticeDto dto = new SmsSendNoticeDto();
            dto.setTempType(smsType);
            dto.setUsers(users);
            dto.setTempParams(tempParams);
            smsSendService.asyncSend(dto);
        } catch (CustomException e) {
            log.info("短信发送失败:{}", e.getResultJson());
            throw new CustomException("短信发送失败，请稍后重试");
        }
    }

    /**
     * 校验短信码
     *
     * @param phone
     * @param smsCaptcha
     */
    public void validSmsCaptcha(String phone, String smsCaptcha) {
        if (StringUtils.isBlank(smsCaptcha)) {
            throw new CustomException("短信验证码错误");
        }
        String key = CaptchaConst.SEND_SMS_CAPTCHA + phone;
        String smsCode = redisService.get(key);
        if (StringUtils.isBlank(smsCode) || !smsCaptcha.equals(smsCode)) {
            throw new CustomException("短信验证码错误");
        }
    }

    /**
     * 验证短信发送次数
     *
     * @param phone
     */
    private void validSmsCaptchaTotal(String phone) {
        String key = CaptchaConst.SEND_SMS_CAPTCHA_TOTAL + phone;
        int total = 0;
        String totalResult = redisService.get(key);
        if (StringUtils.isNotBlank(totalResult)) {
            total = Integer.parseInt(totalResult);
        }
        if (total > 3) {
            throw new CustomException("短信发送次数上限，请联系客服人员。");
        }
        redisService.set(key, String.valueOf(total));
    }
}
