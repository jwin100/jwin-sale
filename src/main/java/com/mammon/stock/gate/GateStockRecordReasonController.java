package com.mammon.stock.gate;

import com.mammon.common.ResultJson;
import com.mammon.stock.service.StockRecordReasonService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/gate/stock/record-reason")
public class GateStockRecordReasonController {

    @Resource
    private StockRecordReasonService stockRecordReasonService;

    @GetMapping("/list")
    public ResultJson list(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId) {
        return ResultJson.ok(stockRecordReasonService.findAll(merchantNo));
    }
}
