package com.mammon.cashier.controller;

import com.mammon.common.ResultJson;
import com.mammon.cashier.domain.dto.CashierDiscountDto;
import com.mammon.cashier.service.CashierDiscountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单折扣规则
 */
@RestController
@RequestMapping("/cashier/discount")
public class CashierDiscountController {

    @Resource
    private CashierDiscountService cashierDiscountService;

    @PutMapping
    public ResultJson edit(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @RequestBody CashierDiscountDto dto) {
        String discountId = cashierDiscountService.edit(merchantNo, dto);
        return ResultJson.ok(discountId);
    }

    @DeleteMapping("/{id}")
    public ResultJson delete(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @PathVariable("id") String id) {
        cashierDiscountService.delete(id);
        return ResultJson.ok();
    }
}
