package com.mammon.clerk.op;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.clerk.domain.dto.PowerCreateDto;
import com.mammon.clerk.domain.dto.RoleDto;
import com.mammon.clerk.domain.entity.RoleEntity;
import com.mammon.clerk.domain.query.RolePageQuery;
import com.mammon.clerk.domain.vo.PowerVo;
import com.mammon.clerk.domain.vo.RoleVo;
import com.mammon.clerk.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/op/clerk/role")
public class OpRoleController {

    @Resource
    private RoleService roleService;

    @PostMapping(value = "/create")
    public ResultJson create(@RequestBody RoleDto dto) {
        roleService.save(0, dto);
        return ResultJson.ok();
    }

    @PostMapping(value = "/edit/{id}")
    public ResultJson edit(@PathVariable("id") String id,
                           @RequestBody RoleDto dto) {
        roleService.edit(0, id, dto);
        return ResultJson.ok();
    }

    /**
     * 设置商户注册时默认使用角色
     *
     * @return
     */
    @PutMapping("/edit-default-status/{id}")
    public ResultJson editDefaultStatus(@PathVariable("id") String id) {
        roleService.editDefaultStatus(id);
        return ResultJson.ok();
    }

    /**
     * 修改系统预设角色状态
     *
     * @return
     */
    @PutMapping("/status/{id}")
    public ResultJson editStatus(@PathVariable("id") String id,
                                 @RequestParam int status) {
        roleService.editStatus(0, id, status);
        return ResultJson.ok();
    }

    @PostMapping("/delete/{id}")
    public ResultJson delete(@PathVariable("id") String id) {
        roleService.delete(0, id);
        return ResultJson.ok();
    }

    @GetMapping("/info")
    public ResultJson<RoleEntity> info(@RequestParam String id) {
        RoleEntity vo = roleService.info(0, id);
        return ResultJson.ok(vo);
    }

    @GetMapping("/page")
    public ResultJson<PageVo<RoleVo>> page(RolePageQuery query) {
        PageVo<RoleVo> vo = roleService.page(0, query);
        return ResultJson.ok(vo);
    }

    @PostMapping("/power-create")
    public ResultJson powerMenuCreate(@RequestParam String roleId,
                                      @RequestBody List<PowerCreateDto> dtos) {
        roleService.powerCreate(0, roleId, dtos);
        return ResultJson.ok();
    }

    @GetMapping("/power")
    public ResultJson<List<PowerVo>> powerMenu() {
        List<PowerVo> vos = roleService.powerMenu(0, null);
        return ResultJson.ok(vos);
    }

    @GetMapping("/power-checked")
    public ResultJson<List<String>> powerMenuChecked(@RequestParam String roleId) {
        List<String> vos = roleService.powerMenuChecked(0, roleId, null);
        return ResultJson.ok(vos);
    }
}
