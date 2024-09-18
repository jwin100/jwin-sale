package com.mammon.merchant.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.merchant.domain.dto.MerchantStoreDto;
import com.mammon.merchant.domain.query.MerchantStoreQuery;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.service.MerchantStoreService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/merchant/merchant-store")
public class MerchantStoreController {

    @Resource
    private MerchantStoreService merchantStoreService;

    @PostMapping
    public ResultJson<Void> create(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @RequestBody MerchantStoreDto dto) {
        merchantStoreService.create(merchantNo, dto);
        return ResultJson.ok();
    }

    @PutMapping("/{id}")
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestBody MerchantStoreDto dto) {
        merchantStoreService.edit(id, dto);
        return ResultJson.ok();
    }

    @PutMapping("/status/{id}")
    public ResultJson<Void> editStatus(@RequestHeader long merchantNo,
                                       @RequestHeader long storeNo,
                                       @RequestHeader String accountId,
                                       @PathVariable("id") String id,
                                       @RequestParam int status) {
        merchantStoreService.editStatus(id, status);
        return ResultJson.ok();
    }

    @DeleteMapping("/{id}")
    public ResultJson<Void> delete(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @PathVariable("id") String id) {
        merchantStoreService.delete(id);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson<MerchantStoreVo> detail(@RequestHeader long merchantNo,
                                              @RequestHeader long storeNo,
                                              @RequestHeader String accountId,
                                              @PathVariable("id") String id) {
        return ResultJson.ok(merchantStoreService.findById(id));
    }

    @GetMapping("/page")
    public ResultJson<PageVo<MerchantStoreVo>> page(@RequestHeader long merchantNo,
                                                    @RequestHeader long storeNo,
                                                    @RequestHeader String accountId,
                                                    MerchantStoreQuery query) {
        PageVo<MerchantStoreVo> result = merchantStoreService.page(merchantNo, query);
        return ResultJson.ok(result);
    }
}
