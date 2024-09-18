package com.mammon.sms.gate;

import com.mammon.common.ResultJson;
import com.mammon.sms.service.SmsTemplateService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/gate/sms/template")
public class GateSmsTemplateController {

    @Resource
    private SmsTemplateService smsTemplateService;
    
    /**
     * 模板列表
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @return
     */
    @GetMapping("/list/{tempType}")
    public ResultJson tempList(@RequestHeader long merchantNo,
                               @RequestHeader long storeNo,
                               @RequestHeader String accountId,
                               @PathVariable("tempType") int tempType) {
        return ResultJson.ok(smsTemplateService.findAllByTempType(merchantNo, tempType));
    }
}
