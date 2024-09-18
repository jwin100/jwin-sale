package com.mammon.clerk.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.clerk.domain.dto.ResourceDto;
import com.mammon.clerk.domain.query.ResourcePageQuery;
import com.mammon.clerk.domain.vo.ResourceVo;
import com.mammon.clerk.service.ResourceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 资源管理
 */
@RestController
@RequestMapping("/clerk/resource")
public class ResourceController {

    @Resource
    private ResourceService resourceService;

    @PostMapping
    public ResultJson create(@RequestBody ResourceDto dto) {
        resourceService.save(dto);
        return ResultJson.ok();
    }

    @PutMapping("/{id}")
    public ResultJson edit(@PathVariable("id") String id,
                           @RequestBody ResourceDto dto) {
        resourceService.edit(id, dto);
        return ResultJson.ok();
    }

    @DeleteMapping("/{id}")
    public ResultJson delete(@PathVariable("id") String id) {
        resourceService.delete(id);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson info(@PathVariable("id") String id) {
        ResourceVo vo = resourceService.info(id);
        return ResultJson.ok(vo);
    }

    @GetMapping("/page")
    public ResultJson page(ResourcePageQuery query) {
        PageVo<ResourceVo> vo = resourceService.page(query);
        return ResultJson.ok(vo);
    }
}
