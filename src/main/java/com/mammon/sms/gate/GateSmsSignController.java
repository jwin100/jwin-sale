package com.mammon.sms.gate;

import com.mammon.common.ResultJson;
import com.mammon.sms.service.SmsSignService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/gate/sms/sign")
public class GateSmsSignController {

    //如果需要切换签名，让用户新建签名,等审核通过了把默认使用切过来

    @Resource
    private SmsSignService smsSignService;

    /**
     * 获取默认使用签名
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @return
     */
    @GetMapping("/default-sign")
    public ResultJson defaultSign(@RequestHeader long merchantNo,
                                  @RequestHeader long storeNo,
                                  @RequestHeader String accountId) {
        return ResultJson.ok(smsSignService.findByDefaultStatus(merchantNo));
    }

    /**
     * 签名列表
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @return
     */
    @GetMapping("/list")
    public ResultJson signList(@RequestHeader long merchantNo,
                               @RequestHeader long storeNo,
                               @RequestHeader String accountId) {
        return ResultJson.ok(smsSignService.findAllByMerchantNo(merchantNo));
    }
}
