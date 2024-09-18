package com.mammon.stock.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.stock.domain.query.StockSkuPageQuery;
import com.mammon.stock.domain.vo.StockSkuDetailListVo;
import com.mammon.stock.domain.vo.StockSpuListVo;
import com.mammon.stock.service.StockSkuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2024/7/10 20:05
 */
@RestController
@RequestMapping("/stock/sku")
public class StockSkuController {

    @Resource
    private StockSkuService stockSkuService;

    @GetMapping("/page")
    public ResultJson<PageVo<StockSkuDetailListVo>> page(@RequestHeader long merchantNo,
                                                         @RequestHeader long storeNo,
                                                         @RequestHeader String accountId,
                                                         StockSkuPageQuery query) {
        PageVo<StockSkuDetailListVo> result = stockSkuService.page(merchantNo, storeNo, accountId, query);
        return ResultJson.ok(result);
    }
}
