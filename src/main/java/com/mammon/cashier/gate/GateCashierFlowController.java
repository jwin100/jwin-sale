package com.mammon.cashier.gate;

import com.mammon.cashier.domain.entity.CashierFlowEntity;
import com.mammon.common.ResultJson;
import com.mammon.cashier.service.CashierFlowService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单流水号规则
 */
@RestController
@RequestMapping("/gate/cashier/flow")
public class GateCashierFlowController {

    @Resource
    private CashierFlowService cashierFlowService;

    @GetMapping
    public ResultJson<CashierFlowEntity> info(@RequestHeader long merchantNo,
                                              @RequestHeader long storeNo,
                                              @RequestHeader String accountId) {
        return ResultJson.ok(cashierFlowService.info(merchantNo));
    }

    @GetMapping("/customer-no")
    public ResultJson<String> getCustomerNo(@RequestHeader long merchantNo,
                                            @RequestHeader long storeNo) {
        return ResultJson.ok(cashierFlowService.getCustomerNo(merchantNo));
    }
}
