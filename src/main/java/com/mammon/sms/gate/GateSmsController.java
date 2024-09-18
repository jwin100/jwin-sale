package com.mammon.sms.gate;

import com.mammon.common.ResultJson;
import com.mammon.sms.service.SmsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/gate/sms")
public class GateSmsController {

    @Resource
    private SmsService smsService;

    @GetMapping("/exists")
    public ResultJson smsExists(@RequestHeader long merchantNo,
                                @RequestHeader long storeNo,
                                @RequestHeader String accountId) {
        return ResultJson.ok(smsService.smsExists(merchantNo, storeNo, accountId));
    }

    @PostMapping("/enable")
    public ResultJson smsEnable(@RequestHeader long merchantNo,
                                @RequestHeader long storeNo,
                                @RequestHeader String accountId) {
        smsService.smsEnable(merchantNo, storeNo, accountId);
        return ResultJson.ok();
    }

    @GetMapping
    public ResultJson smsInfo(@RequestHeader long merchantNo,
                              @RequestHeader long storeNo,
                              @RequestHeader String accountId) {
        return ResultJson.ok(smsService.smsInfo(merchantNo));
    }
}