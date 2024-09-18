package com.mammon.sms.open;

import com.mammon.sms.service.SmsSendService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dcl
 * @since 2023/12/11 15:46
 */
@RequestMapping("/open/sms-callback")
public class OpenSmsCallbackController {

    @Resource
    private SmsSendService smsSendService;

    @RequestMapping("/report/{channelCode}")
    public Object callback(@PathVariable("channelCode") String channelCode,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        return smsSendService.smsCallback(channelCode, request, response);
    }
}
