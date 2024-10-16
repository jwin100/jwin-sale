package com.mammon.biz.controller;

import com.mammon.biz.domain.dto.ActionLogDto;
import com.mammon.biz.service.ActionLogService;
import com.mammon.common.ResultJson;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2024/10/16 19:34
 */
@RestController
@RequestMapping("/biz/action-log")
public class ActionLogController {

    @Resource
    private ActionLogService actionLogService;

    @PostMapping
    public ResultJson<Void> create(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @RequestBody ActionLogDto dto) {
        actionLogService.save(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok();
    }
}
