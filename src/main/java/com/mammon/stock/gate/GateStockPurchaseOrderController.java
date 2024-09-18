package com.mammon.stock.gate;

import com.mammon.common.ResultJson;
import com.mammon.stock.service.StockPurchaseOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 采购入库
 */
@RestController
@RequestMapping("/gate/stock/purchase-order")
public class GateStockPurchaseOrderController {

    @Resource
    private StockPurchaseOrderService stockPurchaseOrderService;

    @GetMapping("/list")
    public ResultJson list(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId) {
        return ResultJson.ok(stockPurchaseOrderService.list(merchantNo, storeNo, accountId));
    }
}
