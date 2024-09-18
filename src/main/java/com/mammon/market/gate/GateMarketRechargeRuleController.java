package com.mammon.market.gate;

import com.mammon.common.ResultJson;
import com.mammon.market.service.MarketRechargeRuleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/gate/market/recharge-rule")
public class GateMarketRechargeRuleController {

    @Resource
    private MarketRechargeRuleService marketRechargeRuleService;

    @GetMapping("/list")
    public ResultJson list(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId) {
        return ResultJson.ok(marketRechargeRuleService.findAll(merchantNo, storeNo, accountId));
    }
}