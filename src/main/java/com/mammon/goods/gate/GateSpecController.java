package com.mammon.goods.gate;

import com.mammon.common.ResultJson;
import com.mammon.goods.domain.entity.SpecEntity;
import com.mammon.goods.domain.query.SpecKeyQuery;
import com.mammon.goods.service.SpecService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @date 2023-02-24 13:05:35
 */
@RestController
@RequestMapping("/gate/goods/spec")
public class GateSpecController {

    @Resource
    private SpecService specService;

    @GetMapping("/list")
    public ResultJson keyList(@RequestHeader long merchantNo,
                              @RequestHeader long storeNo,
                              @RequestHeader String accountId,
                              SpecKeyQuery query) {
        List<SpecEntity> list = specService.findAll(merchantNo, query);
        return ResultJson.ok(list);
    }
}
