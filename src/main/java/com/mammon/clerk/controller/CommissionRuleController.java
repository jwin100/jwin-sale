package com.mammon.clerk.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.clerk.domain.dto.CommissionRuleDto;
import com.mammon.clerk.domain.query.CommissionRuleQuery;
import com.mammon.clerk.domain.vo.CommissionRuleVo;
import com.mammon.clerk.service.CommissionRuleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2024/4/7 16:53
 */
@RestController
@RequestMapping("/clerk/commission-rule")
public class CommissionRuleController {

    @Resource
    private CommissionRuleService commissionRuleService;

    @PostMapping
    public ResultJson<Void> create(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @RequestBody CommissionRuleDto dto) {
        commissionRuleService.create(merchantNo, dto);
        return ResultJson.ok();
    }

    @PutMapping("/{id}")
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestBody CommissionRuleDto dto) {
        commissionRuleService.edit(id, dto);
        return ResultJson.ok();
    }

    @PutMapping("/status/{id}")
    public ResultJson<Void> editStatus(@RequestHeader long merchantNo,
                                       @RequestHeader long storeNo,
                                       @RequestHeader String accountId,
                                       @PathVariable("id") String id,
                                       @RequestParam int status) {
        commissionRuleService.editStatus(id, status);
        return ResultJson.ok();
    }

    @DeleteMapping("/{id}")
    public ResultJson<Void> delete(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @PathVariable("id") String id) {
        commissionRuleService.delete(id);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson<CommissionRuleVo> detail(@RequestHeader long merchantNo,
                                               @RequestHeader long storeNo,
                                               @RequestHeader String accountId,
                                               @PathVariable("id") String id) {
        CommissionRuleVo vo = commissionRuleService.findById(id);
        return ResultJson.ok(vo);
    }

    @GetMapping("/page")
    public ResultJson<PageVo<CommissionRuleVo>> page(@RequestHeader long merchantNo,
                                                     @RequestHeader long storeNo,
                                                     @RequestHeader String accountId,
                                                     CommissionRuleQuery query) {
        PageVo<CommissionRuleVo> pageVo = commissionRuleService.page(merchantNo, query);
        return ResultJson.ok(pageVo);
    }
}
