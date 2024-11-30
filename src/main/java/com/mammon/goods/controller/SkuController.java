package com.mammon.goods.controller;

import com.mammon.common.ResultJson;
import com.mammon.goods.domain.dto.SkuSingleDto;
import com.mammon.goods.domain.dto.SpuDto;
import com.mammon.goods.service.SkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2024/11/28 21:08
 */
@RestController
@RequestMapping("/goods/sku")
@Slf4j
public class SkuController {

    @Resource
    private SkuService skuService;

    @PostMapping
    public ResultJson<Void> create(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @Validated @RequestBody SkuSingleDto dto) {
        skuService.save(merchantNo, dto);
        return ResultJson.ok();
    }

    @PutMapping("/{id}")
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestBody SkuSingleDto dto) {
        skuService.edit(merchantNo, id, dto);
        return ResultJson.ok();
    }
}
