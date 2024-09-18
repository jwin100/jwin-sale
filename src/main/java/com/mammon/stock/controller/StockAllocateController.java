package com.mammon.stock.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.stock.domain.vo.StockAllocateVo;
import com.mammon.stock.domain.dto.StockAllocateDto;
import com.mammon.stock.domain.dto.StockAllocateExamineDto;
import com.mammon.stock.domain.query.StockAllocateQuery;
import com.mammon.stock.service.StockAllocateService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 调拨
 */
@RestController
@RequestMapping("/stock/allocate")
public class StockAllocateController {

    @Resource
    private StockAllocateService stockAllocateService;

    /**
     * 创建调拨单
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
                                   @RequestBody StockAllocateDto dto) {
        //如果是调入,等待调出方审核并调出
        //如果是调出，直接调出
        stockAllocateService.create(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok();
    }

    /**
     * 修改调拨单
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     * @return
     */
    @PutMapping("/{id}")
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestBody StockAllocateDto dto) {
        //如果是调入,等待调出方审核并调出
        //如果是调出，直接调出
        stockAllocateService.edit(merchantNo, storeNo, accountId, id, dto);
        return ResultJson.ok();
    }

    /**
     * 审核
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto     1:审核通过，0审核驳回
     * @return
     */
    @PutMapping("/examine/{id}")
    public ResultJson<Void> examine(@RequestHeader long merchantNo,
                                    @RequestHeader long storeNo,
                                    @RequestHeader String accountId,
                                    @PathVariable("id") String id,
                                    @RequestBody StockAllocateExamineDto dto) {
        stockAllocateService.examine(merchantNo, storeNo, accountId, id, dto);
        return ResultJson.ok();
    }

    @PutMapping("/close/{id}")
    public ResultJson<Void> close(@RequestHeader long merchantNo,
                                  @RequestHeader long storeNo,
                                  @RequestHeader String accountId,
                                  @PathVariable("id") String id) {
        stockAllocateService.getClose(merchantNo, storeNo, accountId, id);
        return ResultJson.ok();
    }

    /**
     * 调出
     *
     * @return
     */
    @PutMapping("/out-stock/{id}")
    public ResultJson<Void> outStock(@RequestHeader long merchantNo,
                                     @RequestHeader long storeNo,
                                     @RequestHeader String accountId,
                                     @PathVariable("id") String id,
                                     @RequestBody StockAllocateDto dto) {
        stockAllocateService.outStock(merchantNo, storeNo, accountId, id, dto);
        return ResultJson.ok();
    }

    /**
     * 调入
     *
     * @return
     */
    @PutMapping("/in-stock/{id}")
    public ResultJson<Void> inStock(@RequestHeader long merchantNo,
                                    @RequestHeader long storeNo,
                                    @RequestHeader String accountId,
                                    @PathVariable("id") String id,
                                    @RequestBody StockAllocateDto dto) {
        stockAllocateService.inStock(merchantNo, storeNo, accountId, id, dto);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson<StockAllocateVo> info(@RequestHeader long merchantNo,
                                            @RequestHeader long storeNo,
                                            @RequestHeader String accountId,
                                            @PathVariable("id") String id) {
        return ResultJson.ok(stockAllocateService.info(merchantNo, storeNo, accountId, id));
    }

    @GetMapping("/page")
    public ResultJson<PageVo<StockAllocateVo>> page(@RequestHeader long merchantNo,
                                                    @RequestHeader long storeNo,
                                                    @RequestHeader String accountId,
                                                    StockAllocateQuery dto) {
        return ResultJson.ok(stockAllocateService.page(merchantNo, storeNo, accountId, dto));
    }

}
