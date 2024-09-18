package com.mammon.cashier.gate;

import com.mammon.common.ResultJson;
import com.mammon.cashier.service.CashierDiscountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单折扣
 */
@RestController
@RequestMapping("/gate/cashier/discount")
public class GateCashierDiscountController {

    @Resource
    private CashierDiscountService cashierDiscountService;

    @GetMapping("/list")
    public ResultJson list(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId) {
        return ResultJson.ok(cashierDiscountService.list(merchantNo));
    }
}
