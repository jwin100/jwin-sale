package com.mammon.goods.gate;

import com.mammon.common.ResultJson;
import com.mammon.goods.domain.vo.UnitVo;
import com.mammon.goods.service.UnitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @date 2023-02-24 13:05:56
 */
@RestController
@RequestMapping("/gate/goods/unit")
public class GateUnitController {

    @Resource
    private UnitService unitService;

    @GetMapping("/list")
    public ResultJson list(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId) {
        List<UnitVo> list = unitService.findAll(merchantNo);
        return ResultJson.ok(list);
    }
}
