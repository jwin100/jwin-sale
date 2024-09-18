package com.mammon.summary.controller;

import com.mammon.common.ResultJson;
import com.mammon.summary.domain.query.SummaryAccountQuery;
import com.mammon.summary.domain.query.SummaryStoreQuery;
import com.mammon.summary.domain.vo.SummaryAccountDashVo;
import com.mammon.summary.domain.vo.SummaryStoreDashVo;
import com.mammon.summary.domain.vo.SummaryStoreVo;
import com.mammon.summary.service.SummaryStoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 门店业绩统计
 *
 * @author dcl
 * @since 2024/4/26 14:50
 */
@RestController
@RequestMapping("/summary-store")
public class SummaryStoreController {
    // 主店且是门店长可以选择门店查看各门店业绩，其他人只能看本门店业绩

    @Resource
    private SummaryStoreService summaryStoreService;

    /**
     * 门店业绩总览
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param query
     * @return
     */
    @GetMapping("/dash")
    public ResultJson<SummaryStoreDashVo> dash(@RequestHeader long merchantNo,
                                               @RequestHeader long storeNo,
                                               @RequestHeader String accountId,
                                               SummaryStoreQuery query) {
        return ResultJson.ok(summaryStoreService.dash(merchantNo, storeNo, accountId, query));
    }

    /**
     * 门店业绩列表
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param query
     * @return
     */
    @GetMapping("/list")
    public ResultJson<List<SummaryStoreVo>> list(@RequestHeader long merchantNo,
                                                 @RequestHeader long storeNo,
                                                 @RequestHeader String accountId,
                                                 SummaryStoreQuery query) {
        return ResultJson.ok(summaryStoreService.list(merchantNo, storeNo, accountId, query));
    }
}
