package com.mammon.sms.op;

import com.mammon.common.ResultJson;
import com.mammon.sms.service.SmsSignService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/op/sms/sms-sign")
public class OpSmsSignController {

    @Resource
    private SmsSignService smsSignService;

    @GetMapping("/list")
    public ResultJson list() {
        return ResultJson.ok(smsSignService.findAllByMerchantNo(0));
    }
}
