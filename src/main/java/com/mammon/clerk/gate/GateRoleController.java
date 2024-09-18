package com.mammon.clerk.gate;

import com.mammon.common.ResultJson;
import com.mammon.clerk.domain.dto.PowerCreateDto;
import com.mammon.clerk.domain.entity.RoleEntity;
import com.mammon.clerk.domain.vo.PowerVo;
import com.mammon.clerk.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @date 2023-02-26 16:22:48
 */
@RestController
@RequestMapping("/gate/clerk/role")
public class GateRoleController {

    @Resource
    private RoleService roleService;


    @GetMapping("/list")
    public ResultJson list(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId) {
        List<RoleEntity> vo = roleService.list(merchantNo);
        return ResultJson.ok(vo);
    }

    @PostMapping("/power-checked/{id}")
    public ResultJson powerMenuCreate(@RequestHeader long merchantNo,
                                      @RequestHeader long storeNo,
                                      @RequestHeader String accountId,
                                      @PathVariable("id") String id,
                                      @RequestBody List<PowerCreateDto> dtos) {
        roleService.powerCreate(merchantNo, id, dtos);
        return ResultJson.ok();
    }

    @GetMapping("/power")
    public ResultJson powerMenu(@RequestHeader long merchantNo,
                                @RequestHeader long storeNo,
                                @RequestHeader String accountId) {
        List<PowerVo> vos = roleService.powerMenu(merchantNo, accountId);
        return ResultJson.ok(vos);
    }

    @GetMapping("/power-checked/{roleId}")
    public ResultJson powerMenuChecked(@RequestHeader long merchantNo,
                                       @RequestHeader long storeNo,
                                       @RequestHeader String accountId,
                                       @PathVariable("roleId") String id) {
        List<String> vos = roleService.powerMenuChecked(merchantNo, id, accountId);
        return ResultJson.ok(vos);
    }
}
