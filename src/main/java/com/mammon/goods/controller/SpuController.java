package com.mammon.goods.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.goods.domain.query.SpuPageQuery;
import com.mammon.goods.domain.dto.SpuDto;
import com.mammon.goods.domain.vo.SpuDetailVo;
import com.mammon.goods.domain.vo.SpuListVo;
import com.mammon.goods.service.SpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/goods/spu")
@Slf4j
public class SpuController {

    @Resource
    private SpuService spuService;

    @PostMapping
    public ResultJson<Void> create(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @Validated @RequestBody SpuDto dto) {
        spuService.create(merchantNo, accountId, dto);
        return ResultJson.ok();
    }

    @PutMapping("/{id}")
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestBody SpuDto dto) {
        spuService.edit(merchantNo, accountId, id, dto);
        return ResultJson.ok();
    }

    @PutMapping("/status/{id}")
    public ResultJson<Void> editStatus(@RequestHeader long merchantNo,
                                       @RequestHeader long storeNo,
                                       @RequestHeader String accountId,
                                       @PathVariable("id") String id,
                                       @RequestParam int status) {
        spuService.editStatus(merchantNo, accountId, id, status);
        return ResultJson.ok();
    }

    @PostMapping("/import/{syncStoreNo}")
    public ResultJson<Void> spuImport(@RequestHeader long merchantNo,
                                      @RequestHeader long storeNo,
                                      @RequestHeader String accountId,
                                      @PathVariable("syncStoreNo") long syncStoreNo,
                                      @RequestParam("file") MultipartFile file) {
        spuService.spuImport(merchantNo, accountId, syncStoreNo, file);
        return ResultJson.ok();
    }

    /**
     * 删除商品
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultJson<Void> delete(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @PathVariable("id") String id) {
        spuService.deleted(merchantNo, id);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson<SpuDetailVo> info(@RequestHeader long merchantNo,
                                        @RequestHeader long storeNo,
                                        @RequestHeader String accountId,
                                        @PathVariable("id") String id) {
        return ResultJson.ok(spuService.findDetailById(merchantNo, id));
    }

    @GetMapping("/page")
    public ResultJson<PageVo<SpuListVo>> page(@RequestHeader long merchantNo,
                                              @RequestHeader long storeNo,
                                              @RequestHeader String accountId,
                                              SpuPageQuery query) {
        PageVo<SpuListVo> result = spuService.page(merchantNo, query);
        return ResultJson.ok(result);
    }
}
