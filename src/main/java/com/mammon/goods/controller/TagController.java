package com.mammon.goods.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.goods.domain.dto.TagDto;
import com.mammon.goods.domain.entity.TagEntity;
import com.mammon.goods.domain.query.TagQuery;
import com.mammon.goods.service.TagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/goods/tag")
public class TagController {

    @Resource
    private TagService tagService;

    @PostMapping
    public ResultJson<Void> create(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @RequestBody TagDto dto) {
        tagService.create(merchantNo, dto);
        return ResultJson.ok();
    }

    @PutMapping("/{id}")
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestBody TagDto dto) {
        tagService.edit(merchantNo, id, dto);
        return ResultJson.ok();
    }

    @DeleteMapping("/{id}")
    public ResultJson<Void> delete(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @PathVariable("id") String id) {
        tagService.delete(merchantNo, id);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson<TagEntity> info(@RequestHeader long merchantNo,
                                      @RequestHeader long storeNo,
                                      @RequestHeader String accountId,
                                      @PathVariable("id") String id) {
        return ResultJson.ok(tagService.info(merchantNo, id));
    }

    @GetMapping("/page")
    public ResultJson<PageVo<TagEntity>> page(@RequestHeader long merchantNo,
                                              @RequestHeader long storeNo,
                                              @RequestHeader String accountId,
                                              TagQuery query) {
        return ResultJson.ok(tagService.page(merchantNo, null, query));
    }
}
