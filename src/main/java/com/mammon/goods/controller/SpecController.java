package com.mammon.goods.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.goods.domain.dto.SpecDto;
import com.mammon.goods.domain.dto.SpecValueDto;
import com.mammon.goods.domain.query.SpecKeyQuery;
import com.mammon.goods.service.SpecService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/goods/spec")
public class SpecController {

    @Resource
    private SpecService specService;

    @PostMapping
    public ResultJson create(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @RequestBody SpecDto dto) {
        specService.create(merchantNo, dto);
        return ResultJson.ok(1);
    }

    @PutMapping("/{id}")
    public ResultJson edit(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @PathVariable("id") String id,
                           @RequestBody SpecDto dto) {
        specService.edit(merchantNo, id, dto);
        return ResultJson.ok();
    }

    @PutMapping("/batch-edit")
    public ResultJson batchEdit(@RequestHeader long merchantNo,
                                @RequestHeader long storeNo,
                                @RequestHeader String accountId,
                                @RequestBody List<SpecValueDto> dtos) {
        specService.batchEdit(merchantNo, dtos);
        return ResultJson.ok();
    }

    @DeleteMapping("/{id}")
    public ResultJson delete(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @PathVariable("id") String id) {
        specService.delete(merchantNo, id);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson info(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @PathVariable("id") String id) {
        return ResultJson.ok(specService.findById(merchantNo, id));
    }

    @GetMapping("/list")
    public ResultJson valueInfo(@RequestHeader long merchantNo,
                                @RequestHeader long storeNo,
                                @RequestHeader String accountId,
                                SpecKeyQuery query) {
        return ResultJson.ok(specService.findAll(merchantNo, query));
    }

    @GetMapping("/page")
    public ResultJson page(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           SpecKeyQuery query) {
        PageVo result = specService.page(merchantNo, query);
        return ResultJson.ok(result);
    }
}
