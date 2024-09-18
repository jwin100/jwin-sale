package com.mammon.summary.controller;

import com.mammon.common.ResultJson;
import com.mammon.summary.domain.query.SummaryCashierQuery;
import com.mammon.summary.domain.vo.SummaryCashierDashVo;
import com.mammon.summary.domain.vo.SummaryCashierVo;
import com.mammon.summary.service.SummaryCashierService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 销售统计
 *
 * @author dcl
 * @since 2024/4/26 14:49
 */
@RestController
@RequestMapping("/summary-cashier")
public class SummaryCashierController {

    @Resource
    private SummaryCashierService summaryCashierService;

    /**
     * 门店销售总览
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param query
     * @return
     */
    @GetMapping("/dash")
    public ResultJson<SummaryCashierDashVo> dash(@RequestHeader long merchantNo,
                                                 @RequestHeader long storeNo,
                                                 @RequestHeader String accountId,
                                                 SummaryCashierQuery query) {
        return ResultJson.ok(summaryCashierService.dash(merchantNo, storeNo, accountId, query));
    }

    /**
     * 门店销售列表
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param query
     * @return
     */
    @GetMapping("/list")
    public ResultJson<List<SummaryCashierVo>> list(@RequestHeader long merchantNo,
                                                   @RequestHeader long storeNo,
                                                   @RequestHeader String accountId,
                                                   SummaryCashierQuery query) {
        return ResultJson.ok(summaryCashierService.list(merchantNo, storeNo, accountId, query));
    }
}
