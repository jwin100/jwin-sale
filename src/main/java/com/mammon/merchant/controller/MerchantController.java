package com.mammon.merchant.controller;

import com.mammon.common.ResultJson;
import com.mammon.merchant.domain.dto.MerchantDto;
import com.mammon.merchant.service.MerchantService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2024/1/16 17:36
 */
@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Resource
    private MerchantService merchantService;

    @PutMapping("/{merchantNo}")
    public ResultJson<Void> edit(@PathVariable long merchantNo,
                                 @Validated @RequestBody MerchantDto dto) {
        merchantService.edit(merchantNo, dto);
        return ResultJson.ok();
    }
}
