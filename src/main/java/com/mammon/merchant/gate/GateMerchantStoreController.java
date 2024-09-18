package com.mammon.merchant.gate;

import com.mammon.common.ResultJson;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.service.MerchantStoreService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/gate/merchant/merchant-store")
public class GateMerchantStoreController {

    @Resource
    private MerchantStoreService merchantStoreService;

    @GetMapping("/list")
    public ResultJson<List<MerchantStoreVo>> list(@RequestHeader long merchantNo,
                                                  @RequestHeader long storeNo,
                                                  @RequestHeader String accountId) {
        List<MerchantStoreVo> result = merchantStoreService.findAllByStoreNos(merchantNo, null);
        return ResultJson.ok(result);
    }
}
