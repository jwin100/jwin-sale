package com.mammon.stock.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.stock.domain.query.StockSpuPageQuery;
import com.mammon.stock.domain.vo.StockSpuListVo;
import com.mammon.stock.domain.vo.StockSpuVo;
import com.mammon.stock.service.StockSpuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2024/7/2 11:44
 */
@RestController
@RequestMapping("/stock/spu")
public class StockSpuController {

    @Resource
    private StockSpuService stockSpuService;

    @PutMapping("/status/{spuId}")
    public ResultJson<Void> editStatus(@RequestHeader long merchantNo,
                                       @RequestHeader long storeNo,
                                       @RequestHeader String accountId,
                                       @PathVariable("spuId") String spuId,
                                       @RequestParam int status) {
        stockSpuService.editStatus(merchantNo, storeNo, spuId, status);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson<StockSpuVo> info(@RequestHeader long merchantNo,
                                       @RequestHeader long storeNo,
                                       @RequestHeader String accountId,
                                       @PathVariable("id") String id) {
        return ResultJson.ok(stockSpuService.findDetailBySpuId(merchantNo, storeNo, id));
    }

    @GetMapping("/page")
    public ResultJson<PageVo<StockSpuListVo>> page(@RequestHeader long merchantNo,
                                                   @RequestHeader long storeNo,
                                                   @RequestHeader String accountId,
                                                   StockSpuPageQuery query) {
        PageVo<StockSpuListVo> result = stockSpuService.page(merchantNo, storeNo, accountId, query);
        return ResultJson.ok(result);
    }
}
