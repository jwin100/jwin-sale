package com.mammon.clerk.gate;

import com.mammon.common.ResultJson;
import com.mammon.clerk.domain.vo.ResourceVo;
import com.mammon.clerk.service.ResourceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @date 2023-02-26 00:58:19
 */
@RestController
@RequestMapping("/gate/clerk/resource")
public class GateResourceController {

    @Resource
    private ResourceService resourceService;

    /**
     * 获取菜单列表
     *
     * @param accountId
     * @return
     */
    @GetMapping("/list")
    @DeleteMapping
    public ResultJson list(@RequestHeader String accountId) {
        //根据accId获取到角色，根据角色获取到对应的menu
        List<ResourceVo> resourceList = resourceService.findAllByAccountId(accountId);
        return ResultJson.ok(resourceList);
    }

    @GetMapping("/children-resources/{pid}")
    public ResultJson childrenResource(@PathVariable("pid") String pid) {
        List<ResourceVo> vo = resourceService.findAllByPid(pid);
        return ResultJson.ok(vo);
    }
}
