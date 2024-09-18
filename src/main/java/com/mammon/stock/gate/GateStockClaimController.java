package com.mammon.stock.gate;

import com.mammon.common.ResultJson;
import com.mammon.stock.domain.vo.StockClaimDetailVo;
import com.mammon.stock.domain.query.StockClaimRuleQuery;
import com.mammon.stock.service.StockClaimService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2024/3/14 19:41
 */
@RestController
@RequestMapping("/gate/stock/claim")
public class GateStockClaimController {

    @Resource
    private StockClaimService stockClaimService;

    @GetMapping("/rule")
    public ResultJson<StockClaimDetailVo> claimRule(@RequestHeader long merchantNo,
                                                    @RequestHeader long storeNo,
                                                    @RequestHeader String accountId,
                                                    StockClaimRuleQuery query) {
        return ResultJson.ok(stockClaimService.findClaimRule(merchantNo, query));
    }
}
