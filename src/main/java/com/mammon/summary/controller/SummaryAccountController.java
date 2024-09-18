package com.mammon.summary.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.summary.domain.query.SummaryAccountQuery;
import com.mammon.summary.domain.query.SummaryAccountSelfQuery;
import com.mammon.summary.domain.vo.SummaryAccountDashVo;
import com.mammon.summary.domain.vo.SummaryAccountVo;
import com.mammon.summary.service.SummaryAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 店员业绩统计
 *
 * @author dcl
 * @since 2024/4/26 14:48
 */
@RestController
@RequestMapping("/summary-account")
public class SummaryAccountController {

    @Resource
    private SummaryAccountService summaryAccountService;

    // 统计是给图呢，还是给列表和汇总信息
    // 少个店员提成

    /**
     * 我的业绩
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param query
     * @return
     */
    @GetMapping("/self")
    public ResultJson<SummaryAccountDashVo> self(@RequestHeader long merchantNo,
                                                 @RequestHeader long storeNo,
                                                 @RequestHeader String accountId,
                                                 SummaryAccountSelfQuery query) {
        return ResultJson.ok(summaryAccountService.self(merchantNo, storeNo, accountId, query));
    }

    /**
     * 店员业绩总览
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param query
     * @return
     */
    @GetMapping("/dash")
    public ResultJson<SummaryAccountDashVo> dash(@RequestHeader long merchantNo,
                                                 @RequestHeader long storeNo,
                                                 @RequestHeader String accountId,
                                                 SummaryAccountQuery query) {
        return ResultJson.ok(summaryAccountService.dash(merchantNo, storeNo, accountId, query));
    }

    /**
     * 店员业绩分页列表
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param query
     * @return
     */
    @GetMapping("/page")
    public ResultJson<PageVo<SummaryAccountVo>> page(@RequestHeader long merchantNo,
                                                     @RequestHeader long storeNo,
                                                     @RequestHeader String accountId,
                                                     SummaryAccountQuery query) {
        return ResultJson.ok(summaryAccountService.page(merchantNo, storeNo, accountId, query));
    }
}
