package com.mammon.clerk.open;

import com.mammon.common.ResultJson;
import com.mammon.clerk.domain.dto.CommissionSummaryDto;
import com.mammon.clerk.service.CommissionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2024/4/8 10:26
 */
@RestController
@RequestMapping("/open/clerk/commission")
public class OpenCommissionController {

    @Resource
    private CommissionService commissionService;

    @PostMapping("/summary")
    public ResultJson<Void> summary(@RequestBody(required = false) CommissionSummaryDto dto) {
        commissionService.summary(dto);
        return ResultJson.ok();
    }
}
