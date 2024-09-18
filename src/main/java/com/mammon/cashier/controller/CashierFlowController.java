package com.mammon.cashier.controller;

import com.mammon.common.ResultJson;
import com.mammon.cashier.domain.dto.CashierFlowDto;
import com.mammon.cashier.service.CashierFlowService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单流水号规则
 */
@RestController
@RequestMapping("/cashier/flow")
public class CashierFlowController {

    @Resource
    private CashierFlowService cashierFlowService;

    @PutMapping
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @RequestBody CashierFlowDto dto) {
        cashierFlowService.edit(merchantNo, dto);
        return ResultJson.ok();
    }
}
