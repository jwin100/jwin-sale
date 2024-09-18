package com.mammon.stock.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.stock.domain.vo.StockRecordReasonVo;
import com.mammon.stock.domain.dto.StockRecordReasonDto;
import com.mammon.stock.domain.query.StockRecordReasonQuery;
import com.mammon.stock.service.StockRecordReasonService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/stock/record-reason")
public class StockRecordReasonController {

    @Resource
    private StockRecordReasonService stockRecordReasonService;

    @PostMapping
    public ResultJson<Void> create(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @RequestBody StockRecordReasonDto dto) {
        stockRecordReasonService.create(merchantNo, dto);
        return ResultJson.ok();
    }

    @PutMapping("/{id}")
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestBody StockRecordReasonDto dto) {
        stockRecordReasonService.update(merchantNo, id, dto);
        return ResultJson.ok();
    }

    @DeleteMapping("/{id}")
    public ResultJson<Void> delete(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @PathVariable("id") String id) {
        stockRecordReasonService.delete(merchantNo, id);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson<StockRecordReasonVo> info(@RequestHeader long merchantNo,
                                                @RequestHeader long storeNo,
                                                @RequestHeader String accountId,
                                                @PathVariable("id") String id) {
        return ResultJson.ok(stockRecordReasonService.findById(merchantNo, id));
    }

    @GetMapping("/page")
    public ResultJson<PageVo<StockRecordReasonVo>> page(@RequestHeader long merchantNo,
                                                        @RequestHeader long storeNo,
                                                        @RequestHeader String accountId,
                                                        StockRecordReasonQuery dto) {
        return ResultJson.ok(stockRecordReasonService.page(merchantNo, storeNo, accountId, dto));
    }
}
