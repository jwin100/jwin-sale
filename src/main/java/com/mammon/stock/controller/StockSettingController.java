package com.mammon.stock.controller;

import com.mammon.common.ResultJson;
import com.mammon.stock.domain.dto.StockSettingDto;
import com.mammon.stock.service.StockSettingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2023/12/27 11:20
 */
@RestController
@RequestMapping("/stock/setting")
public class StockSettingController {

    @Resource
    private StockSettingService stockSettingService;

    @PutMapping
    public ResultJson edit(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @RequestBody StockSettingDto dto) {
        stockSettingService.modify(merchantNo, dto);
        return ResultJson.ok();
    }

    @GetMapping
    public ResultJson getByMerchantNo(@RequestHeader long merchantNo,
                                      @RequestHeader long storeNo,
                                      @RequestHeader String accountId) {
        return ResultJson.ok(stockSettingService.findByMerchantNo(merchantNo));
    }
}
