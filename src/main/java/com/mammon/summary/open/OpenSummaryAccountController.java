package com.mammon.summary.open;

import com.mammon.common.ResultJson;
import com.mammon.summary.service.SummaryAccountService;
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
@RequestMapping("/open/summary-account")
public class OpenSummaryAccountController {

    @Resource
    private SummaryAccountService summaryAccountService;

    @PostMapping("/quartz-job")
    public ResultJson<Void> yesterdaySummary(@RequestParam(required = false) LocalDate yesterday) {
        summaryAccountService.yesterdaySummary(yesterday);
        return ResultJson.ok();
    }

}
