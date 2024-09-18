package com.mammon.stock.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.stock.domain.dto.StockPurchaseReplenishDto;
import com.mammon.stock.domain.vo.StockPurchaseOrderVo;
import com.mammon.stock.domain.dto.StockPurchaseOrderDto;
import com.mammon.stock.domain.dto.StockPurchaseOrderExamineDto;
import com.mammon.stock.domain.query.StockPurchaseOrderQuery;
import com.mammon.stock.service.StockPurchaseOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 采购
 */
@RestController
@RequestMapping("/stock/purchase-order")
public class StockPurchaseOrderController {

    @Resource
    private StockPurchaseOrderService stockPurchaseOrderService;

    /**
     * 创建采购单
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     * @return
     */
    @PostMapping
    public ResultJson<Void> create(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @RequestBody StockPurchaseOrderDto dto) {
        stockPurchaseOrderService.create(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok();
    }

    @PutMapping("/{id}")
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestBody StockPurchaseOrderDto dto) {
        stockPurchaseOrderService.edit(merchantNo, storeNo, accountId, id, dto);
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
                                    @RequestBody StockPurchaseOrderExamineDto dto) {
        stockPurchaseOrderService.examine(merchantNo, id, dto);
        return ResultJson.ok();
    }

    @PutMapping("/close/{id}")
    public ResultJson<Void> close(@RequestHeader long merchantNo,
                                  @RequestHeader long storeNo,
                                  @RequestHeader String accountId,
                                  @PathVariable("id") String id) {
        stockPurchaseOrderService.close(merchantNo, id);
        return ResultJson.ok();
    }

    /**
     * 采购入库
     *
     * @return
     */
    @PutMapping("/replenish/{id}")
    public ResultJson<Void> replenish(@RequestHeader long merchantNo,
                                      @RequestHeader long storeNo,
                                      @RequestHeader String accountId,
                                      @PathVariable("id") String id,
                                      @RequestBody List<StockPurchaseReplenishDto> dto) {
        stockPurchaseOrderService.replenish(merchantNo, storeNo, accountId, id, dto);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson<StockPurchaseOrderVo> info(@RequestHeader long merchantNo,
                                                 @RequestHeader long storeNo,
                                                 @RequestHeader String accountId,
                                                 @PathVariable("id") String id) {
        return ResultJson.ok(stockPurchaseOrderService.info(merchantNo, id));
    }

    @GetMapping("/page")
    public ResultJson<PageVo<StockPurchaseOrderVo>> page(@RequestHeader long merchantNo,
                                                         @RequestHeader long storeNo,
                                                         @RequestHeader String accountId,
                                                         StockPurchaseOrderQuery dto) {
        return ResultJson.ok(stockPurchaseOrderService.page(merchantNo, storeNo, accountId, dto));
    }
}
