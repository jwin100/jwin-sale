package com.mammon.summary.gate;

import com.mammon.summary.domain.query.SummaryHomeDashQuery;
import com.mammon.summary.domain.query.SummaryHomeTrendQuery;
import com.mammon.summary.domain.vo.SummaryHomeDashVo;
import com.mammon.summary.domain.vo.SummaryHomeTrendVo;
import com.mammon.common.ResultJson;
import com.mammon.summary.service.SummaryHomeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @since 2024/2/29 13:17
 */
@RestController
@RequestMapping("/gate/summary-home")
public class GateSummaryHomeController {

    @Resource
    private SummaryHomeService summaryHomeService;

    /**
     * 首页概览统计
     *
     * @return
     */
    @GetMapping("/dash")
    public ResultJson<SummaryHomeDashVo> summaryHomeDash(@RequestHeader long merchantNo,
                                                         @RequestHeader long storeNo,
                                                         @RequestHeader String accountId,
                                                         SummaryHomeDashQuery query) {
        SummaryHomeDashVo dashVo = summaryHomeService.summaryHomeDash(merchantNo, storeNo, query);
        return ResultJson.ok(dashVo);
    }

    /**
     * 首页折线图统计
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param query
     * @return
     */
    @GetMapping("/trend")
    public ResultJson<List<SummaryHomeTrendVo>> summaryHomeTrend(@RequestHeader long merchantNo,
                                                                 @RequestHeader long storeNo,
                                                                 @RequestHeader String accountId,
                                                                 SummaryHomeTrendQuery query) {
        List<SummaryHomeTrendVo> list = summaryHomeService.summaryHomeTrend(merchantNo, storeNo, query);
        return ResultJson.ok(list);
    }
}
