package com.mammon.sms.controller;

import com.mammon.common.ResultJson;
import com.mammon.sms.domain.dto.SmsQuery;
import com.mammon.sms.domain.dto.SmsSendRecordDto;
import com.mammon.sms.service.SmsSendItemService;
import com.mammon.sms.service.SmsSendService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sms/send")
public class SmsSendController {

    @Resource
    private SmsSendService smsSendService;

    @Resource
    private SmsSendItemService smsSendItemService;

    @PostMapping
    public ResultJson record(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @RequestBody SmsSendRecordDto dto) {
        smsSendService.syncSend(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok();
    }

    /**
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultJson smsItems(@RequestHeader long merchantNo,
                               @RequestHeader long storeNo,
                               @RequestHeader String accountId,
                               @PathVariable("id") String id) {
        return ResultJson.ok(smsSendItemService.findAllBySmsSendId(merchantNo, storeNo, accountId, id));
    }

    @GetMapping("/page")
    public ResultJson smsPage(@RequestHeader long merchantNo,
                              @RequestHeader long storeNo,
                              @RequestHeader String accountId,
                              SmsQuery dto) {
        return ResultJson.ok(smsSendService.page(merchantNo, storeNo, accountId, dto));
    }
}
