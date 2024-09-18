package com.mammon.cashier.gate;

import com.mammon.common.ResultJson;
import com.mammon.cashier.service.CashierIgnoreService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 抹零规则
 */
@RestController
@RequestMapping("/gate/cashier/ignore")
public class GateCashierIgnoreController {

    @Resource
    private CashierIgnoreService cashierIgnoreService;

    @GetMapping("/set-list")
    public ResultJson getSetList(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId) {
        return ResultJson.ok(cashierIgnoreService.getSetList());
    }

    @GetMapping
    public ResultJson info(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId) {
        return ResultJson.ok(cashierIgnoreService.info(merchantNo));
    }
}
