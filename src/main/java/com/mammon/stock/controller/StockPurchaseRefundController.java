package com.mammon.stock.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.stock.domain.vo.StockPurchaseRefundVo;
import com.mammon.stock.domain.dto.StockPurchaseRefundDto;
import com.mammon.stock.domain.query.StockPurchaseRefundQuery;
import com.mammon.stock.domain.dto.StockPurchaseRefundExamineDto;
import com.mammon.stock.service.StockPurchaseRefundService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 采购退货
 */
@RestController
@RequestMapping("/stock/purchase-refund")
public class StockPurchaseRefundController {

    @Resource
    private StockPurchaseRefundService stockPurchaseRefundService;

    @PostMapping
    public ResultJson<Void> create(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @RequestBody StockPurchaseRefundDto dto) {
        stockPurchaseRefundService.create(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok();
    }

    @PutMapping("/{id}")
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestBody StockPurchaseRefundDto dto) {
        stockPurchaseRefundService.edit(merchantNo, storeNo, accountId, id, dto);
        return ResultJson.ok();
    }

    /**
     * 审核
     *
     * @return
     */
    @PutMapping("/examine/{id}")
    public ResultJson<Void> examine(@RequestHeader long merchantNo,
                                    @RequestHeader long storeNo,
                                    @RequestHeader String accountId,
                                    @PathVariable("id") String id,
                                    @RequestBody StockPurchaseRefundExamineDto dto) {
        stockPurchaseRefundService.examine(merchantNo, id, dto);
        return ResultJson.ok();
    }

    @PutMapping("/close/{id}")
    public ResultJson<Void> close(@RequestHeader long merchantNo,
                                  @RequestHeader long storeNo,
                                  @RequestHeader String accountId,
                                  @PathVariable("id") String id) {
        stockPurchaseRefundService.close(merchantNo, id);
        return ResultJson.ok();
    }

    /**
     * 退货出库
     *
     * @return
     */
    @PutMapping("/expand/{id}")
    public ResultJson<Void> expand(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @PathVariable("id") String id) {
        stockPurchaseRefundService.expand(merchantNo, storeNo, accountId, id);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson<StockPurchaseRefundVo> info(@RequestHeader long merchantNo,
                                                  @RequestHeader long storeNo,
                                                  @RequestHeader String accountId,
                                                  @PathVariable("id") String id) {
        return ResultJson.ok(stockPurchaseRefundService.info(merchantNo, id));
    }

    @GetMapping("/page")
    public ResultJson<PageVo<StockPurchaseRefundVo>> page(@RequestHeader long merchantNo,
                                                          @RequestHeader long storeNo,
                                                          @RequestHeader String accountId,
                                                          StockPurchaseRefundQuery dto) {
        return ResultJson.ok(stockPurchaseRefundService.page(merchantNo, storeNo, accountId, dto));
    }
}
