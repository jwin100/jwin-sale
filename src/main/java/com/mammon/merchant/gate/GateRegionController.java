package com.mammon.merchant.gate;

import com.mammon.common.ResultJson;
import com.mammon.merchant.domain.vo.RegionTreeVo;
import com.mammon.merchant.service.RegionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/gate/region")
public class GateRegionController {

    @Resource
    private RegionService regionService;

    @GetMapping("/list")
    public ResultJson<List<RegionTreeVo>> getList() {
        return ResultJson.ok(regionService.findAll());
    }
}
