package com.mammon.market.controller;

import com.mammon.common.ResultJson;
import com.mammon.market.domain.dto.MarketRechargeRuleDto;
import com.mammon.market.domain.query.MarketRechargeRuleQuery;
import com.mammon.market.service.MarketRechargeRuleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/market/recharge-rule")
public class MarketRechargeRuleController {

    @Resource
    private MarketRechargeRuleService marketRechargeRuleService;

    @PostMapping
    public ResultJson create(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @RequestBody MarketRechargeRuleDto dto) {
        marketRechargeRuleService.create(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok();
    }

    @PutMapping("/{id}")
    public ResultJson edit(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @PathVariable("id") String id,
                           @RequestBody MarketRechargeRuleDto dto) {
        marketRechargeRuleService.edit(merchantNo, storeNo, accountId, id, dto);
        return ResultJson.ok();
    }

    @PutMapping("/status/{id}")
    public ResultJson editStatus(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestParam int status) {
        marketRechargeRuleService.editStatus(merchantNo, storeNo, accountId, id, status);
        return ResultJson.ok();
    }

    @DeleteMapping("/{id}")
    public ResultJson delete(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @PathVariable("id") String id) {
        marketRechargeRuleService.deleted(merchantNo, id);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson info(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @PathVariable("id") String id) {
        return ResultJson.ok(marketRechargeRuleService.findById(id));
    }

    @GetMapping("/page")
    public ResultJson page(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           MarketRechargeRuleQuery query) {
        return ResultJson.ok(marketRechargeRuleService.page(merchantNo, storeNo, accountId, query));
    }
}