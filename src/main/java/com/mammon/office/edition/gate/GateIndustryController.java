package com.mammon.office.edition.gate;

import com.mammon.common.ResultJson;
import com.mammon.office.edition.domain.enums.AbilityCodeConst;
import com.mammon.merchant.service.MerchantIndustryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dcl
 * @date 2023-02-06 15:31:53
 */
@RestController
@RequestMapping("/gate/office-edition/industry")
public class GateIndustryController {

    @Resource
    private MerchantIndustryService merchantIndustryService;

    /**
     * 获取商户激活的付费功能
     *
     * @return
     */
    @GetMapping
    @DeleteMapping
    public ResultJson merchantChargeAbility(@RequestHeader long merchantNo) {
        return ResultJson.ok(merchantIndustryService.merchantChargeAbility(merchantNo));
    }
}
