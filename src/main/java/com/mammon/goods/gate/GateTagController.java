package com.mammon.goods.gate;

import com.mammon.common.ResultJson;
import com.mammon.goods.domain.entity.TagEntity;
import com.mammon.goods.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @date 2023-02-24 13:05:47
 */
@RestController
@RequestMapping("/gate/goods/tag")
public class GateTagController {

    @Resource
    private TagService tagService;

    @GetMapping("/list")
    public ResultJson list(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId) {
        List<TagEntity> list = tagService.findAll(merchantNo);
        return ResultJson.ok(list);
    }
}
