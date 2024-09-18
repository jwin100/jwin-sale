package com.mammon.market.controller;

import com.mammon.common.ResultJson;
import com.mammon.market.domain.dto.MarketTimeCardRuleDto;
import com.mammon.market.domain.query.MarketTimeCardRuleQuery;
import com.mammon.market.service.MarketTimeCardRuleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/market/time-card-rule")
public class MarketTimeCardRuleController {

    @Resource
    private MarketTimeCardRuleService marketTimeCardRuleService;

    @PostMapping
    public ResultJson create(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @RequestBody MarketTimeCardRuleDto dto) {
        marketTimeCardRuleService.create(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok();
    }

    @PutMapping("/{id}")
    public ResultJson edit(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @PathVariable("id") String id,
                           @RequestBody MarketTimeCardRuleDto dto) {
        marketTimeCardRuleService.edit(merchantNo, storeNo, accountId, id, dto);
        return ResultJson.ok();
    }

    @PutMapping("/status/{id}")
    public ResultJson editStatus(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestParam int status) {
        marketTimeCardRuleService.editStatus(merchantNo, storeNo, accountId, id, status);
        return ResultJson.ok();
    }

    @DeleteMapping("/{id}")
    public ResultJson delete(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @PathVariable("id") String id) {
        marketTimeCardRuleService.deleted(merchantNo, id);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson info(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @PathVariable("id") String id) {
        return ResultJson.ok(marketTimeCardRuleService.findById(id));
    }

    @GetMapping("/page")
    public ResultJson page(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           MarketTimeCardRuleQuery query) {

        return ResultJson.ok(marketTimeCardRuleService.page(merchantNo, storeNo, accountId, query));
    }
}
