package com.mammon.cashier.controller;

import com.mammon.common.ResultJson;
import com.mammon.cashier.service.CashierIgnoreService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 抹零规则
 */
@RestController
@RequestMapping("/cashier/ignore")
public class CashierIgnoreController {

    @Resource
    private CashierIgnoreService cashierIgnoreService;

    @PutMapping("/{type}")
    public ResultJson edit(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @PathVariable("type") int type) {
        cashierIgnoreService.edit(merchantNo, type);
        return ResultJson.ok();
    }
}
