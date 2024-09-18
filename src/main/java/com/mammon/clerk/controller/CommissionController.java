package com.mammon.clerk.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.clerk.domain.dto.CommissionSummaryDto;
import com.mammon.clerk.domain.query.CommissionQuery;
import com.mammon.clerk.domain.vo.CommissionVo;
import com.mammon.clerk.service.CommissionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2024/4/8 10:17
 */
@RestController
@RequestMapping("/clerk/commission")
public class CommissionController {

    @Resource
    private CommissionService commissionService;

    /**
     * 手动计算员工提成
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     * @return
     */
    @PutMapping("/summary")
    public ResultJson<Void> summary(@RequestHeader long merchantNo,
                                    @RequestHeader long storeNo,
                                    @RequestHeader String accountId,
                                    @RequestBody CommissionSummaryDto dto) {
        commissionService.summaryReuse(merchantNo, dto);
        return ResultJson.ok();
    }

    @GetMapping("/page")
    public ResultJson<PageVo<CommissionVo>> page(@RequestHeader long merchantNo,
                                                 @RequestHeader long storeNo,
                                                 @RequestHeader String accountId,
                                                 CommissionQuery query) {
        PageVo<CommissionVo> pageVo = commissionService.page(merchantNo, storeNo, accountId, query);
        return ResultJson.ok(pageVo);
    }
}
