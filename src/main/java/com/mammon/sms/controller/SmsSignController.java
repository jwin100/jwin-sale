package com.mammon.sms.controller;

import com.mammon.common.ResultJson;
import com.mammon.sms.domain.dto.SmsSignDto;
import com.mammon.sms.service.SmsSignService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sms/sign")
public class SmsSignController {

    //如果需要切换签名，让用户新建签名,等审核通过了把默认使用切过来

    @Resource
    private SmsSignService smsSignService;

    /**
     * @return
     */
    @PostMapping
    public ResultJson create(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @RequestBody SmsSignDto dto) {
        smsSignService.create(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok();
    }

    /**
     * @return
     */
    @PutMapping("/{id}")
    public ResultJson edit(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @PathVariable("id") String id,
                           @RequestBody SmsSignDto dto) {
        smsSignService.edit(merchantNo, id, dto);
        return ResultJson.ok();
    }

    /**
     * 设置默认使用签名
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @return
     */
    @PutMapping("/set-default-sign/{id}")
    public ResultJson setDefaultSign(@RequestHeader long merchantNo,
                                     @RequestHeader long storeNo,
                                     @RequestHeader String accountId,
                                     @PathVariable("id") String id) {
        smsSignService.editDefaultStatus(merchantNo, id);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson signInfo(@RequestHeader long merchantNo,
                               @RequestHeader long storeNo,
                               @RequestHeader String accountId,
                               @PathVariable("id") String id) {
        return ResultJson.ok(smsSignService.findById(merchantNo, id));
    }
}
