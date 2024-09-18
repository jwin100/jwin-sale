package com.mammon.clerk.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.clerk.domain.dto.RoleDto;
import com.mammon.clerk.domain.entity.RoleEntity;
import com.mammon.clerk.domain.query.RolePageQuery;
import com.mammon.clerk.domain.vo.RoleVo;
import com.mammon.clerk.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/clerk/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @PostMapping
    public ResultJson create(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @RequestBody RoleDto dto) {
        roleService.save(merchantNo, dto);
        return ResultJson.ok();
    }

    @PutMapping(value = "/{id}")
    public ResultJson edit(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @PathVariable("id") String id,
                           @RequestBody RoleDto dto) {
        roleService.edit(merchantNo, id, dto);
        return ResultJson.ok();
    }

    @PutMapping("/status/{id}")
    public ResultJson editStatus(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestParam int status) {
        roleService.editStatus(merchantNo, id, status);
        return ResultJson.ok();
    }

    @DeleteMapping("/{id}")
    public ResultJson delete(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @PathVariable("id") String id) {
        roleService.delete(merchantNo, id);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson info(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @PathVariable("id") String id) {
        RoleEntity vo = roleService.info(merchantNo, id);
        return ResultJson.ok(vo);
    }

    @GetMapping("/page")
    public ResultJson page(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           RolePageQuery query) {
        PageVo<RoleVo> vo = roleService.page(merchantNo, query);
        return ResultJson.ok(vo);
    }
}
