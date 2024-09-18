package com.mammon.stock.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.stock.domain.vo.StockInventoryDetailVo;
import com.mammon.stock.domain.vo.StockInventoryPageVo;
import com.mammon.stock.domain.dto.StockInventoryCreateDto;
import com.mammon.stock.domain.dto.StockInventoryFinishDto;
import com.mammon.stock.domain.query.StockInventoryQuery;
import com.mammon.stock.service.StockInventoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2024/4/1 17:18
 */
@RestController
@RequestMapping("/stock/inventory")
public class StockInventoryController {

    @Resource
    private StockInventoryService stockInventoryService;

    /**
     * 创建盘点
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     * @return
     */
    @PostMapping
    public ResultJson<String> create(@RequestHeader long merchantNo,
                                     @RequestHeader long storeNo,
                                     @RequestHeader String accountId,
                                     @RequestBody StockInventoryCreateDto dto) {
        String id = stockInventoryService.create(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok(id);
    }

    /**
     * 盘点完成
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param id
     * @param dto
     * @return
     */
    @PutMapping("/finish/{id}")
    public ResultJson<Void> finish(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @PathVariable("id") String id,
                                   @RequestBody StockInventoryFinishDto dto) {
        stockInventoryService.finish(merchantNo, storeNo, id, dto);
        return ResultJson.ok();
    }

    /**
     * 取消盘点
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    public ResultJson<Void> cancel(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @PathVariable("id") String id) {
        stockInventoryService.cancel(id);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson<StockInventoryDetailVo> detail(@RequestHeader long merchantNo,
                                                     @RequestHeader long storeNo,
                                                     @RequestHeader String accountId,
                                                     @PathVariable("id") String id) {
        return ResultJson.ok(stockInventoryService.findById(id));
    }

    @GetMapping("/page")
    public ResultJson<PageVo<StockInventoryPageVo>> page(@RequestHeader long merchantNo,
                                                         @RequestHeader long storeNo,
                                                         @RequestHeader String accountId,
                                                         StockInventoryQuery query) {
        PageVo<StockInventoryPageVo> pageVo = stockInventoryService.page(merchantNo, storeNo, accountId, query);
        return ResultJson.ok(pageVo);
    }
}
