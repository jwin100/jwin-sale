package com.mammon.market.gate;

import com.mammon.common.ResultJson;
import com.mammon.market.service.MarketTimeCardRuleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/gate/market/time-card-rule")
public class GateMarketTimeCardRuleController {

    @Resource
    private MarketTimeCardRuleService marketTimeCardRuleService;

    @GetMapping("/list")
    public ResultJson list(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId) {

        return ResultJson.ok(marketTimeCardRuleService.list(merchantNo, storeNo, accountId));
    }
}
