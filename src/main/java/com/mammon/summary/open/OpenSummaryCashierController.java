package com.mammon.summary.open;

import com.mammon.summary.service.SummaryCashierService;
import com.mammon.common.ResultJson;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * @author dcl
 * @since 2024/2/29 10:42
 */
@RestController
@RequestMapping("/open/summary-cashier")
public class OpenSummaryCashierController {

    @Resource
    private SummaryCashierService summaryCashierService;


    @PostMapping("/quartz-job")
    public ResultJson<Void> yesterdaySummary(@RequestParam(required = false) LocalDate yesterday) {
        summaryCashierService.yesterdaySummary(yesterday);
        return ResultJson.ok();
    }
}
