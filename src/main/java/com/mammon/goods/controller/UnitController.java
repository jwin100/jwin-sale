package com.mammon.goods.controller;

import com.mammon.common.ResultJson;
import com.mammon.goods.domain.dto.UnitDto;
import com.mammon.goods.domain.query.UnitQuery;
import com.mammon.goods.service.UnitService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/goods/unit")
public class UnitController {

    @Resource
    private UnitService unitService;

    @PostMapping
    public ResultJson create(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @RequestBody UnitDto dto) {
        unitService.create(merchantNo, dto);
        return ResultJson.ok();
    }

    @PutMapping("/{id}")
    public ResultJson edit(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @PathVariable("id") String id,
                           @RequestBody UnitDto dto) {
        unitService.edit(merchantNo, id, dto);
        return ResultJson.ok();
    }

    @DeleteMapping("/{id}")
    public ResultJson delete(@RequestHeader long merchantNo,
                              @RequestHeader long storeNo,
                              @RequestHeader String accountId,
                              @PathVariable("id") String id) {
        unitService.delete(merchantNo, id);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson info(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @PathVariable("id") String id) {
        return ResultJson.ok(unitService.findById(merchantNo, id));
    }

    @GetMapping("/page")
    public ResultJson page(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           UnitQuery query) {
        return ResultJson.ok(unitService.page(merchantNo, null, query));
    }
}
