package com.mammon.sms.controller;

import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.sms.domain.dto.SmsTempDto;
import com.mammon.sms.domain.dto.SmsTempQuery;
import com.mammon.sms.service.SmsTemplateService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sms/template")
public class SmsTemplateController {

    @Resource
    private SmsTemplateService smsTemplateService;

    /**
     * 创建模板
     *
     * @return
     */
    @PostMapping
    public ResultJson create(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @RequestBody SmsTempDto dto) {
        smsTemplateService.create(merchantNo, dto);
        return ResultJson.ok();
    }

    @PutMapping("/{id}")
    public ResultJson edit(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @PathVariable("id") String id,
                           @RequestBody SmsTempDto dto) {
        smsTemplateService.edit(merchantNo, id, dto);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson tempInfo(@RequestHeader long merchantNo,
                               @RequestHeader long storeNo,
                               @RequestHeader String accountId,
                               @PathVariable("id") String id) {
        return ResultJson.ok(smsTemplateService.findById(merchantNo, id));
    }

    @GetMapping("/page")
    public ResultJson tempPage(@RequestHeader long merchantNo,
                               @RequestHeader long storeNo,
                               @RequestHeader String accountId,
                               SmsTempQuery dto) {
        PageVo result = smsTemplateService.page(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok(result);
    }
}
