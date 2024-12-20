package com.mammon.goods.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.goods.domain.dto.SkuSingleDto;
import com.mammon.goods.domain.dto.SpuDto;
import com.mammon.goods.domain.query.SkuPageQuery;
import com.mammon.goods.domain.vo.SkuDetailListVo;
import com.mammon.goods.domain.vo.SkuDetailVo;
import com.mammon.goods.domain.vo.SpuDetailVo;
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
        skuService.save(merchantNo, accountId, dto);
        return ResultJson.ok();
    }

    @PutMapping("/{skuId}")
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("skuId") String skuId,
                                 @RequestBody SkuSingleDto dto) {
        skuService.edit(merchantNo, accountId, skuId, dto);
        return ResultJson.ok();
    }

    @DeleteMapping("/{skuId}")
    public ResultJson<Void> delete(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @PathVariable("skuId") String skuId) {
        skuService.deleteBySkuId(merchantNo, skuId);
        return ResultJson.ok();
    }

    @GetMapping("/{skuId}")
    public ResultJson<SkuDetailVo> info(@RequestHeader long merchantNo,
                                        @RequestHeader long storeNo,
                                        @RequestHeader String accountId,
                                        @PathVariable("skuId") String skuId) {
        return ResultJson.ok(skuService.findDetailById(merchantNo, skuId));
    }

    @GetMapping("/page")
    public ResultJson<PageVo<SkuDetailListVo>> page(@RequestHeader long merchantNo,
                                                    @RequestHeader long storeNo,
                                                    @RequestHeader String accountId,
                                                    SkuPageQuery query) {
        return ResultJson.ok(skuService.page(merchantNo, storeNo, accountId, query));
    }
}
