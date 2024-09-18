package com.mammon.clerk.op;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.clerk.domain.dto.ResourceDto;
import com.mammon.clerk.domain.query.ResourcePageQuery;
import com.mammon.clerk.domain.vo.ResourceVo;
import com.mammon.clerk.service.ResourceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/op/clerk/resource")
public class OpResourceController {

    @Resource
    private ResourceService resourceService;

    @PostMapping("/create")
    public ResultJson create(@RequestBody ResourceDto dto) {
        resourceService.save(dto);
        return ResultJson.ok();
    }

    @PostMapping("/edit/{id}")
    public ResultJson edit(@PathVariable("id") String id,
                           @RequestBody ResourceDto dto) {
        resourceService.edit(id, dto);
        return ResultJson.ok();
    }

    @PostMapping("/delete/{id}")
    public ResultJson delete(@PathVariable("id") String id) {
        resourceService.delete(id);
        return ResultJson.ok();
    }

    @GetMapping("/info")
    public ResultJson<ResourceVo> info(@RequestParam String id) {
        ResourceVo vo = resourceService.info(id);
        return ResultJson.ok(vo);
    }

    @GetMapping("/page")
    public ResultJson page(ResourcePageQuery query) {
        PageVo<ResourceVo> vo = resourceService.page(query);
        return ResultJson.ok(vo);
    }

    @GetMapping("/list")
    public ResultJson list() {
        List<ResourceVo> vo = resourceService.list();
        return ResultJson.ok(vo);
    }

    @GetMapping("/children-resources")
    public ResultJson<List<ResourceVo>> childrenResource(@RequestParam(defaultValue = "1") String pid) {
        List<ResourceVo> vo = resourceService.findAllByPid(pid);
        return ResultJson.ok(vo);
    }
}
