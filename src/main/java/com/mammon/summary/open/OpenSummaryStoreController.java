package com.mammon.summary.open;

import com.mammon.common.ResultJson;
import com.mammon.summary.service.SummaryStoreService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * @author dcl
 * @since 2024/4/22 16:12
 */
@RestController
@RequestMapping("/open/summary-store")
public class OpenSummaryStoreController {

    @Resource
    private SummaryStoreService summaryStoreService;

    @PostMapping("/quartz-job")
    public ResultJson<Void> yesterdaySummary(@RequestParam(required = false) LocalDate yesterday) {
        summaryStoreService.yesterdaySummary(yesterday);

        return ResultJson.ok();
    }
}
